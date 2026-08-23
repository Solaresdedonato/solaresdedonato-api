package ar.com.solaresdedonato.api.adapter.out.repository;

import ar.com.solaresdedonato.api.adapter.out.jpa.UsuarioAdminJpaRepository;
import ar.com.solaresdedonato.api.adapter.out.mapper.UsuarioAdminMapper;
import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;
import ar.com.solaresdedonato.api.core.ports.repository.UsuarioAdminRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioAdminRepository implements UsuarioAdminRepositoryPort {

    private final UsuarioAdminJpaRepository jpaRepository;
    private final UsuarioAdminMapper mapper;

    @Override
    public Optional<UsuarioAdmin> findById(Long id) {
        return jpaRepository.findByIdAndEnableTrue(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UsuarioAdmin> findByEmail(String email) {
        return jpaRepository.findByEmailIgnoreCaseAndEnableTrue(email).map(mapper::toDomain);
    }

    @Override
    public UsuarioAdmin save(UsuarioAdmin domain) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(domain)));
    }
}
