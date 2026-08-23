-- Los videos ahora también pueden importarse desde Drive (enlazados a Drive, NUNCA
-- descargados — ver ArchivoExternoPort.construirUrlReproduccion / plan de ingesta),
-- así que "origen Drive solo para foto" ya no aplica.
--
-- Se dropea en vez de relajar a un "tipo IN (...)": tipo ya está limitado a
-- ('foto','video') por ck_media_url_por_tipo (V1), así que una versión relajada del
-- CHECK sería siempre verdadera — dropearlo es lo honesto.
ALTER TABLE contenido_media DROP CONSTRAINT ck_media_origen_drive_solo_foto;
