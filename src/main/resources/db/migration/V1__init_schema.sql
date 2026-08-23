-- usuario_admin: usuarios del backoffice (login propio, JWT emitido por esta API)
CREATE TABLE usuario_admin (
    id                BIGSERIAL PRIMARY KEY,
    email             VARCHAR(180) NOT NULL,
    password_hash     VARCHAR(100) NOT NULL,     -- BCrypt
    nombre            VARCHAR(160) NOT NULL,
    rol               VARCHAR(20)  NOT NULL DEFAULT 'ADMINISTRADOR' CHECK (rol IN ('ADMINISTRADOR')),
    ultimo_login_date TIMESTAMPTZ,
    enable            BOOLEAN NOT NULL DEFAULT TRUE,
    stamp_app VARCHAR(60) NOT NULL, stamp_user VARCHAR(120) NOT NULL, stamp_date TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE UNIQUE INDEX ux_usuario_admin_email ON usuario_admin (lower(email)) WHERE enable = TRUE;

-- desarrollo
CREATE TABLE desarrollo (
    id                       BIGSERIAL PRIMARY KEY,
    slug                     VARCHAR(160) NOT NULL,   -- 'solares-pinamar' — no puede ser 'admin' ni 'resumen' (reservados, ver DesarrolloController)
    nombre                   VARCHAR(200) NOT NULL,
    zona                     VARCHAR(150) NOT NULL,
    direccion                VARCHAR(300) NOT NULL,
    estado                   VARCHAR(20)  NOT NULL CHECK (estado IN ('en-venta','preventa','en-obra','entregado','proximamente')),
    descripcion              TEXT NOT NULL,
    features                 JSONB NOT NULL DEFAULT '[]'::jsonb,   -- [{"clave":"ubicacion","titulo":"Ubicación","texto":"..."}, ...] siempre 4
    cercanias                JSONB NOT NULL DEFAULT '{}'::jsonb,   -- {"educacion":[...],"transporte":[...],"comercios":[...],"salud":[...]}
    instrumento_tokenizacion BOOLEAN NOT NULL DEFAULT FALSE,
    instrumento_renta_fija   BOOLEAN NOT NULL DEFAULT FALSE,
    publicado                BOOLEAN NOT NULL DEFAULT FALSE,   -- "Guardar borrador" vs "Publicar desarrollo" del backoffice
    imagen_portada_url       VARCHAR(500),                     -- cache de lectura; fuente de verdad es contenido_media.es_portada
    enable                   BOOLEAN NOT NULL DEFAULT TRUE,
    stamp_app VARCHAR(60) NOT NULL, stamp_user VARCHAR(120) NOT NULL, stamp_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_desarrollo_features_shape CHECK (jsonb_typeof(features) = 'array' AND jsonb_array_length(features) = 4),
    CONSTRAINT ck_desarrollo_cercanias_shape CHECK (jsonb_typeof(cercanias) = 'object' AND cercanias ?& array['educacion','transporte','comercios','salud'])
);
CREATE UNIQUE INDEX ux_desarrollo_slug ON desarrollo (slug) WHERE enable = TRUE;
CREATE INDEX ix_desarrollo_estado ON desarrollo (estado) WHERE enable = TRUE;
CREATE INDEX ix_desarrollo_publicado ON desarrollo (publicado) WHERE enable = TRUE;

-- contenido_media: une "imagen principal + galería" del form de desarrollo con la "biblioteca de contenido" —
-- son el mismo concepto (una imagen/video con categoría, asociada a un desarrollo); es_portada marca la de portada.
CREATE TABLE contenido_media (
    id            BIGSERIAL PRIMARY KEY,
    desarrollo_id BIGINT REFERENCES desarrollo(id),  -- nullable: contenido 'institucional' puede no pertenecer a un desarrollo
    tipo          VARCHAR(10) NOT NULL CHECK (tipo IN ('foto','video')),
    titulo        VARCHAR(200) NOT NULL,
    categoria     VARCHAR(20) NOT NULL CHECK (categoria IN ('fachada','interior','amenities','obra','drone','institucional')),
    descripcion   TEXT,
    archivo_url   VARCHAR(500),   -- foto: url tras subir el archivo
    video_url     VARCHAR(500),   -- video: link YouTube/Vimeo
    es_portada    BOOLEAN NOT NULL DEFAULT FALSE,
    orden         SMALLINT NOT NULL DEFAULT 0,
    enable        BOOLEAN NOT NULL DEFAULT TRUE,
    stamp_app VARCHAR(60) NOT NULL, stamp_user VARCHAR(120) NOT NULL, stamp_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_media_url_por_tipo CHECK (
        (tipo = 'foto'  AND archivo_url IS NOT NULL AND video_url IS NULL) OR
        (tipo = 'video' AND video_url  IS NOT NULL AND archivo_url IS NULL)
    ),
    CONSTRAINT ck_media_portada_solo_foto CHECK (NOT es_portada OR tipo = 'foto')
);
CREATE INDEX ix_media_desarrollo ON contenido_media (desarrollo_id) WHERE enable = TRUE;
CREATE UNIQUE INDEX ux_media_portada_unica ON contenido_media (desarrollo_id) WHERE es_portada = TRUE AND enable = TRUE;

-- consulta_contacto (alta pública, resto backoffice)
CREATE TABLE consulta_contacto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL, apellido VARCHAR(120) NOT NULL, email VARCHAR(180) NOT NULL, telefono VARCHAR(40) NOT NULL,
    proyecto_interes VARCHAR(200),  -- texto libre, NO es FK a desarrollo (incluye opciones genéricas: "Tokenización inmobiliaria", etc.)
    mensaje TEXT NOT NULL,
    enable BOOLEAN NOT NULL DEFAULT TRUE,
    stamp_app VARCHAR(60) NOT NULL, stamp_user VARCHAR(120) NOT NULL, stamp_date TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_consulta_contacto_stamp_date ON consulta_contacto (stamp_date DESC);

-- registro_broker (alta pública, resto backoffice)
CREATE TABLE registro_broker (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL, email VARCHAR(180) NOT NULL, telefono VARCHAR(40) NOT NULL,
    inmobiliaria VARCHAR(200) NOT NULL, matricula VARCHAR(80),
    experiencia VARCHAR(40) CHECK (experiencia IS NULL OR experiencia IN ('Menos de 2 años','2 a 5 años','5 a 10 años','Más de 10 años')),
    zona_operacion VARCHAR(80) NOT NULL,       -- sin CHECK: lista de zonas es contenido editorial, no lógica de negocio
    tipo_operaciones JSONB NOT NULL DEFAULT '[]'::jsonb,   -- subset de ["vivienda","inversion","renta","vacacional"]
    operaciones_cerradas VARCHAR(40),
    mensaje TEXT,
    enable BOOLEAN NOT NULL DEFAULT TRUE,
    stamp_app VARCHAR(60) NOT NULL, stamp_user VARCHAR(120) NOT NULL, stamp_date TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_registro_broker_tipo_operaciones CHECK (tipo_operaciones <@ '["vivienda","inversion","renta","vacacional"]'::jsonb)
);
CREATE INDEX ix_registro_broker_stamp_date ON registro_broker (stamp_date DESC);
