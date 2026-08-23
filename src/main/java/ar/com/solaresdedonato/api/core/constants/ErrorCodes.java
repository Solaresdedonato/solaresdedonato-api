package ar.com.solaresdedonato.api.core.constants;

public final class ErrorCodes {

    // ========== BAD REQUEST (01000-01199) ==========
    public static final String ERROR_VALIDACION = "01000";
    public static final String DESARROLLO_SLUG_RESERVADO = "01001";
    public static final String CONTENIDO_ARCHIVO_REQUERIDO = "01002";
    public static final String CONTENIDO_VIDEO_URL_REQUERIDO = "01003";
    public static final String CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO = "01004";
    public static final String CONTENIDO_ARCHIVO_DEMASIADO_GRANDE = "01005";

    // ========== NOT FOUND (01200-01399) ==========
    public static final String ENDPOINT_NO_ENCONTRADO = "01206";
    public static final String USUARIO_ADMIN_NO_ENCONTRADO = "01200";
    public static final String DESARROLLO_NO_ENCONTRADO = "01201";
    public static final String CONTENIDO_MEDIA_NO_ENCONTRADO = "01202";
    public static final String CONSULTA_CONTACTO_NO_ENCONTRADO = "01203";
    public static final String REGISTRO_BROKER_NO_ENCONTRADO = "01204";
    public static final String ARCHIVO_DRIVE_NO_ENCONTRADO = "01205";

    // ========== CONFLICT (01400-01599) ==========
    public static final String DESARROLLO_SLUG_YA_EXISTE = "01400";
    public static final String CONTENIDO_DRIVE_YA_IMPORTADO = "01401";

    // ========== BUSINESS / REGLAS EXTERNAS (01600-01699) ==========
    public static final String DRIVE_SIN_PERMISO = "01600";
    public static final String DRIVE_NO_CONFIGURADO = "01601";

    // ========== AUTHENTICATION & AUTHORIZATION (01800-01899) ==========
    public static final String ERROR_AUTENTICACION = "01800";
    public static final String TOKEN_INVALIDO = "01801";
    public static final String ACCESO_DENEGADO = "01802";
    public static final String CREDENCIALES_INVALIDAS = "01803";

    // ========== INTERNAL (01900-01999) ==========
    public static final String ERROR_INTERNO = "01900";

    private ErrorCodes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
