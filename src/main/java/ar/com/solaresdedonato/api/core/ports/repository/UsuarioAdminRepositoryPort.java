package ar.com.solaresdedonato.api.core.ports.repository;

import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;

import java.util.Optional;

public interface UsuarioAdminRepositoryPort {
    Optional<UsuarioAdmin> findById(Long id);

    Optional<UsuarioAdmin> findByEmail(String email);

    UsuarioAdmin save(UsuarioAdmin domain);
}
