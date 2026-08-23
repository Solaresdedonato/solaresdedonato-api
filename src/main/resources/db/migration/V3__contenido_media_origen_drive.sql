-- Trazabilidad e idempotencia de la ingesta desde Google Drive: Drive es FUENTE de
-- ingesta, no hosting (ver plan de diseño) — archivo_url sigue siendo una URL propia
-- servida por /media/**, ahora relativa (ver comentario más abajo).
ALTER TABLE contenido_media ADD COLUMN origen_drive_file_id VARCHAR(120);

COMMENT ON COLUMN contenido_media.origen_drive_file_id IS
    'fileId de Google Drive del que se importó la foto. NULL para subidas multipart.';

-- Parcial WHERE enable = TRUE, igual que el resto de los índices únicos del esquema:
-- reimportar una foto después de una baja lógica tiene que estar permitido.
CREATE UNIQUE INDEX ux_media_origen_drive
    ON contenido_media (origen_drive_file_id)
    WHERE origen_drive_file_id IS NOT NULL AND enable = TRUE;

-- Consistente con ck_media_portada_solo_foto: un video (link de YouTube/Vimeo) nunca
-- viene de una importación de Drive.
ALTER TABLE contenido_media
    ADD CONSTRAINT ck_media_origen_drive_solo_foto
    CHECK (origen_drive_file_id IS NULL OR tipo = 'foto');

-- LocalFileStorageAdapter pasa a devolver un path relativo ("/media/desarrollo-1/x.jpg")
-- en vez de una URL absoluta con host — desacopla las imágenes del dominio de la API
-- (útil justo antes del primer deploy: no hay filas en producción, pero si quedó algo
-- de las pruebas locales de agosto, esto lo normaliza en vez de dejarlo roto).
UPDATE contenido_media
   SET archivo_url = regexp_replace(archivo_url, '^https?://[^/]+(/[^/]+)?(/media/.*)$', '\2')
 WHERE archivo_url LIKE 'http%';

UPDATE desarrollo
   SET imagen_portada_url = regexp_replace(imagen_portada_url, '^https?://[^/]+(/[^/]+)?(/media/.*)$', '\2')
 WHERE imagen_portada_url LIKE 'http%';
