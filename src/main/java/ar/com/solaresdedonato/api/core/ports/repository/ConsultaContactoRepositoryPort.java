package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;

import java.util.Optional;

public interface ConsultaContactoRepositoryPort {
    PageResult<ConsultaContacto> findAll(PageQuery pageQuery);

    Optional<ConsultaContacto> findById(Long id);

    ConsultaContacto save(ConsultaContacto domain);

    void deleteById(Long id);
}
