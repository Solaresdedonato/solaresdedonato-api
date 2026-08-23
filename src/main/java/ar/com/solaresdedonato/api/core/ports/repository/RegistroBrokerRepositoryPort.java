package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;

import java.util.Optional;

public interface RegistroBrokerRepositoryPort {
    PageResult<RegistroBroker> findAll(PageQuery pageQuery);

    Optional<RegistroBroker> findById(Long id);

    RegistroBroker save(RegistroBroker domain);

    void deleteById(Long id);
}
