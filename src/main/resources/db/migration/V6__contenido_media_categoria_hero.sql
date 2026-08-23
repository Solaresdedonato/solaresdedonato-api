-- Nueva categoría 'hero' para las imágenes del carrusel de inicio del sitio público.
-- A diferencia del resto de las categorías, este contenido no pertenece a ningún
-- desarrollo (desarrollo_id ya es nullable desde V1 — pensado justo para esto) y el
-- campo orden sí importa acá: define la secuencia del carrusel.
ALTER TABLE contenido_media DROP CONSTRAINT contenido_media_categoria_check;
ALTER TABLE contenido_media ADD CONSTRAINT contenido_media_categoria_check
    CHECK (categoria IN ('fachada','interior','amenities','obra','drone','institucional','hero'));

-- Consulta frecuente del hero público: todas las 'hero' habilitadas, en orden.
CREATE INDEX ix_contenido_media_hero ON contenido_media (orden, id) WHERE categoria = 'hero' AND enable = TRUE;
