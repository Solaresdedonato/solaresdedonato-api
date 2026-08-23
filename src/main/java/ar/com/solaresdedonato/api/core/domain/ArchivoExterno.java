package ar.com.solaresdedonato.api.core.domain;

import java.time.LocalDateTime;

/**
 * Metadata de un archivo listado en el origen de ingesta externo (hoy Google Drive,
 * ver {@code core/ports/service/ArchivoExternoPort}). Deliberadamente vendor-neutral:
 * nada acá menciona Drive, así que el día que cambie el origen este record no se toca.
 */
public record ArchivoExterno(
        String id,
        String nombre,
        String mimeType,
        Long tamanioBytes,
        Integer anchoPx,
        Integer altoPx,
        LocalDateTime fechaCreacion,
        boolean tieneMiniatura
) {
}
