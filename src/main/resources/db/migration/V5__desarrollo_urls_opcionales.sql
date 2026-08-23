-- Enlaces opcionales del desarrollo (botones de la ficha pública: showroom virtual,
-- brochure/planos, avance de obra, solicitar información). Ninguno es obligatorio;
-- si vienen null, el botón correspondiente no se muestra (ver DesarrolloAccionesBotones).
ALTER TABLE desarrollo
    ADD COLUMN showroom_virtual_url       VARCHAR(500),
    ADD COLUMN brochure_planos_url        VARCHAR(500),
    ADD COLUMN avance_obra_url            VARCHAR(500),
    ADD COLUMN solicitar_informacion_url  VARCHAR(500);
