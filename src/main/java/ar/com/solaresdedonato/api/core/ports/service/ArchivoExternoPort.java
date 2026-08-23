package ar.com.solaresdedonato.api.core.ports.service;

import ar.com.solaresdedonato.api.core.domain.ArchivoDescargado;
import ar.com.solaresdedonato.api.core.domain.ArchivoExterno;
import ar.com.solaresdedonato.api.core.ports.CursorPageResult;

import java.util.Optional;

/**
 * Origen de ingesta de archivos externo (hoy Google Drive) — vendor-neutral a propósito,
 * el {@code core} no debe saber que del otro lado hay Google. Es fuente de ingesta, no
 * hosting: lo que se descarga acá se guarda después vía {@link FileStoragePort}.
 */
public interface ArchivoExternoPort {

    CursorPageResult<ArchivoExterno> listar(String nombreContiene, String pageToken, int size);

    Optional<ArchivoExterno> obtenerMetadata(String archivoId);

    /** {@code maxBytes} rechaza por tamaño antes de bajar un solo byte, además de lo que
     *  ya haya validado el caller contra la metadata. */
    ArchivoDescargado descargar(String archivoId, long maxBytes);

    Optional<byte[]> descargarMiniatura(String archivoId);

    /** URL reproducible/embebible del archivo, servida DIRECTO por el proveedor externo
     *  (nunca se descarga) — hoy solo la usan los videos: a diferencia de una foto, no
     *  tiene sentido bajar y re-alojar un video entero. Requiere que el archivo puntual
     *  esté compartido como "cualquiera con el link" en Drive; el acceso de solo-lectura
     *  del service account no alcanza para que un visitante del sitio público lo vea. */
    String construirUrlReproduccion(String archivoId);
}
