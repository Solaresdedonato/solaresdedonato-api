package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.domain.ResumenDesarrollos;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;

import java.util.Optional;

public interface DesarrolloRepositoryPort {
    PageResult<Desarrollo> findPublicados(String estado, String zona, PageQuery pageQuery);

    PageResult<Desarrollo> findAdmin(String estado, String zona, Boolean publicado, PageQuery pageQuery);

    Optional<Desarrollo> findBySlugPublicado(String slug);

    Optional<Desarrollo> findById(Long id);

    boolean existsBySlug(String slug);

    boolean existsBySlugExcludingId(String slug, Long id);

    Desarrollo save(Desarrollo domain);

    void deleteById(Long id);

    ResumenDesarrollos resumen();
}
