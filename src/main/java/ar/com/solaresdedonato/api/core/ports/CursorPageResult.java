package ar.com.solaresdedonato.api.core.ports;

import java.util.List;

/**
 * Paginación por cursor, para orígenes que no soportan offset/total (Google Drive pagina
 * con un {@code pageToken} opaco y no devuelve {@code totalElements}). Deliberadamente
 * distinto de {@link PageResult}: forzar ese contrato acá obligaría a inventar un total
 * falso.
 */
public record CursorPageResult<T>(List<T> content, String nextPageToken) {
}
