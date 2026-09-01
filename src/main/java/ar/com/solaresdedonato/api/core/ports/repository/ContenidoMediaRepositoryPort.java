package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ContenidoMediaRepositoryPort {
    List<ContenidoMedia> findByDesarrolloId(Long desarrolloId);

    /** Imágenes del carrusel de inicio, en orden de carrusel. */
    List<ContenidoMedia> findHeroHabilitadas();

    /** Para asignarle el próximo orden a una hero nueva (se agrega al final). */
    long countHeroHabilitadas();

    PageResult<ContenidoMedia> findByFiltro(Long desarrolloId, String tipo, String categoria, PageQuery pageQuery);

    Optional<ContenidoMedia> findById(Long id);

    Optional<ContenidoMedia> findPortadaByDesarrolloId(Long desarrolloId);

    /** Idempotencia acotada al destino: la MISMA foto puede reusarse en otro
     *  desarrollo o categoria (ej. hero + un desarrollo), pero no duplicarse dos
     *  veces en el mismo destino. */
    Optional<ContenidoMedia> findByOrigenDriveFileIdAndDesarrolloIdAndCategoria(
            String driveFileId, Long desarrolloId, String categoria);

    /** De la lista de fileIds de Drive recibida, mapa fileId -> id del contenido_media ya
     *  importado (una sola consulta). Las claves ausentes son "no importado todavía". */
    Map<String, Long> findContenidoMediaIdsPorDriveFileId(List<String> driveFileIds);

    ContenidoMedia save(ContenidoMedia domain);

    void deleteById(Long id);
}
