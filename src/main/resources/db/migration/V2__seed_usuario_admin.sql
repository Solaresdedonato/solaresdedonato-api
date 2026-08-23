-- Usuario admin inicial para poder loguearse al backoffice sin un endpoint de alta
-- (no hay self-signup: usuario_admin se crea a mano vía seed/SQL directo).
-- Credenciales de dev — CAMBIAR el password antes de un despliegue real.
--   email:    admin@solaresdedonato.com.ar
--   password: 8CZem5qt5a9sKvuz
INSERT INTO usuario_admin (email, password_hash, nombre, rol, enable, stamp_app, stamp_user)
VALUES (
    'admin@solaresdedonato.com.ar',
    '$2b$10$ZFt3K16LbUapGx50TF6f3u5Ms6xNpebp.hyl4HLvju46HPRrUvm2C',
    'Administrador',
    'ADMINISTRADOR',
    TRUE,
    'SOLARES-DONATO-API',
    'sistema-seed'
);
