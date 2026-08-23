package ar.com.solaresdedonato.api.core.service;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * Valida archivos de imagen antes de que lleguen a {@code FileStoragePort.store()}, sin
 * importar si los bytes vinieron de un multipart o de una descarga de Google Drive —
 * es exactamente la validación que hoy no existe (el {@code archivoContentType} del
 * request multipart se captura pero nunca se usa).
 *
 * <p>SVG queda deliberadamente fuera de la allow-list: {@code /media/**} lo sirve
 * {@code ResourceHttpRequestHandler} same-origin con la API, y un SVG legítimamente
 * tipado {@code image/svg+xml} puede ejecutar JavaScript — sería XSS almacenado en el
 * dominio de la API. La allow-list de extensión es la defensa real ahí, no
 * {@code X-Content-Type-Options: nosniff} (que ya viene de Spring Security por default),
 * porque el tipo declarado sería el correcto.
 */
@Service
public class ValidadorArchivoImagen {

    private static final Set<String> MIME_PERMITIDOS =
            Set.of("image/jpeg", "image/png", "image/webp", "image/avif");
    private static final Set<String> EXTENSIONES_PERMITIDAS =
            Set.of(".jpg", ".jpeg", ".png", ".webp", ".avif");

    private final long maxBytes;

    public ValidadorArchivoImagen(@Value("${app.storage.max-file-size-bytes}") long maxBytes) {
        this.maxBytes = maxBytes;
    }

    /** Límite configurado, para que un caller (ej. la descarga de Drive) pueda rechazar por
     *  tamaño ANTES de traer un solo byte, usando el mismo límite que después se re-valida acá. */
    public long maxBytes() {
        return maxBytes;
    }

    /** Rechazo temprano por el tamaño que reporta el origen (metadata de Drive), sin descargar nada. */
    public void validarTamanioDeclarado(Long tamanioBytes) {
        if (tamanioBytes != null && tamanioBytes > maxBytes) {
            throw new BadRequestException(
                    "El archivo supera el tamaño máximo permitido",
                    ErrorCodes.CONTENIDO_ARCHIVO_DEMASIADO_GRANDE,
                    Map.of("tamanioBytes", tamanioBytes, "maximoBytes", maxBytes));
        }
    }

    /** Validación completa sobre los bytes ya en memoria: tamaño, extensión, mime declarado y magic number. */
    public void validar(String nombreOriginal, String mimeTypeDeclarado, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new BadRequestException(
                    "El archivo está vacío", ErrorCodes.CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO, Map.of());
        }
        if (bytes.length > maxBytes) {
            throw new BadRequestException(
                    "El archivo supera el tamaño máximo permitido",
                    ErrorCodes.CONTENIDO_ARCHIVO_DEMASIADO_GRANDE,
                    Map.of("tamanioBytes", bytes.length, "maximoBytes", maxBytes));
        }

        String extension = extensionDe(nombreOriginal);
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new BadRequestException(
                    "Extensión de archivo no permitida",
                    ErrorCodes.CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO,
                    Map.of("extension", extension));
        }
        if (mimeTypeDeclarado != null && !mimeTypeDeclarado.isBlank() && !MIME_PERMITIDOS.contains(mimeTypeDeclarado)) {
            throw new BadRequestException(
                    "Tipo de archivo no permitido",
                    ErrorCodes.CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO,
                    Map.of("mimeType", mimeTypeDeclarado));
        }

        // El único chequeo que el cliente no puede mentir: el contenido real del archivo.
        if (detectarMimePorMagicNumber(bytes) == null) {
            throw new BadRequestException(
                    "El contenido del archivo no es una imagen soportada",
                    ErrorCodes.CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO,
                    Map.of());
        }
    }

    private String extensionDe(String nombreOriginal) {
        if (nombreOriginal == null) return "";
        int punto = nombreOriginal.lastIndexOf('.');
        return punto >= 0 ? nombreOriginal.substring(punto).toLowerCase() : "";
    }

    private String detectarMimePorMagicNumber(byte[] b) {
        if (coincidePrefijo(b, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF}, 0)) return "image/jpeg";
        if (coincidePrefijo(b, new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}, 0)) return "image/png";
        if (coincideAscii(b, "RIFF", 0) && coincideAscii(b, "WEBP", 8)) return "image/webp";
        if (coincideAscii(b, "ftypavif", 4)) return "image/avif";
        return null;
    }

    private boolean coincidePrefijo(byte[] b, byte[] firma, int offset) {
        if (b.length < offset + firma.length) return false;
        for (int i = 0; i < firma.length; i++) {
            if (b[offset + i] != firma[i]) return false;
        }
        return true;
    }

    private boolean coincideAscii(byte[] b, String ascii, int offset) {
        return coincidePrefijo(b, ascii.getBytes(StandardCharsets.US_ASCII), offset);
    }
}
