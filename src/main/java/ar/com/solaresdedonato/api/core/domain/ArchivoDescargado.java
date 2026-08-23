package ar.com.solaresdedonato.api.core.domain;

/**
 * Bytes ya descargados del origen externo, listos para {@code FileStoragePort.store()}.
 * Value object de solo lectura: no lleva {@code @Builder} porque no hace falta, y
 * {@code equals}/{@code hashCode} por referencia sobre {@code contenido} es irrelevante
 * acá (nunca se comparan dos instancias entre sí).
 */
public record ArchivoDescargado(String id, String nombre, String mimeType, byte[] contenido) {
}
