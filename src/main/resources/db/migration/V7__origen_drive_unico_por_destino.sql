-- ux_media_origen_drive (V3) era global: una foto de Drive ya importada quedaba
-- bloqueada para SIEMPRE en cualquier otro desarrollo o categoria, sin importar que
-- el destino fuera distinto -- impedia, por ejemplo, reusar una foto del hero en un
-- desarrollo. Pasa a ser unico por (drive_file_id, desarrollo_id, categoria): sigue
-- evitando el duplicado accidental de re-importar la MISMA foto al MISMO destino,
-- pero permite reusarla en otro desarrollo o en el hero.
--
-- COALESCE(desarrollo_id, -1): en un indice unico comun dos NULL nunca chocan entre
-- si, y 'hero'/contenido institucional usan desarrollo_id NULL a proposito -- sin el
-- COALESCE, dos imports de la misma foto al hero no se habrian detectado como
-- duplicado.
DROP INDEX ux_media_origen_drive;

CREATE UNIQUE INDEX ux_media_origen_drive
    ON contenido_media (origen_drive_file_id, COALESCE(desarrollo_id, -1), categoria)
    WHERE origen_drive_file_id IS NOT NULL AND enable = TRUE;
