package ar.com.solaresdedonato.api.core.domain;

/**
 * Un {@link ArchivoExterno} enriquecido con si ya fue importado a la biblioteca de
 * contenido (y con qué id), para que el picker del backoffice pueda grisarlo y decir
 * "ya está en la biblioteca (#42)" sin una consulta aparte por archivo.
 */
public record ArchivoExternoConEstado(ArchivoExterno archivo, boolean yaImportado, Long contenidoMediaId) {
}
