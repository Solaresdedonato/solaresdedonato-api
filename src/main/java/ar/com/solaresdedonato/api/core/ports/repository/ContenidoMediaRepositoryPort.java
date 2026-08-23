package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ContenidoMediaRepositoryPort {
    List<ContenidoMedia> findByDesarrolloId(Long desarrolloId);

    PageResult<ContenidoMedia> findByFiltro(Long desarrolloId, String tipo, String categoria, PageQuery pageQuery);

    Optional<ContenidoMedia> findById(Long id);

    Optional<ContenidoMedia> findPortadaByDesarrolloId(Long desarrolloId);

    Optional<ContenidoMedia> findByOrigenDriveFileId(String driveFileId);

    /** De la lista de fileIds de Drive recibida, mapa fileId -> id del contenido_media ya
     *  importado (una sola consulta). Las claves ausentes son "no importado todavía". */
    Map<String, Long> findContenidoMediaIdsPorDriveFileId(List<String> driveFileIds);

    ContenidoMedia save(ContenidoMedia domain);

    void deleteById(Long id);
}
