package ar.com.solaresdedonato.api.adapter.out.jpa;

import ar.com.solaresdedonato.api.adapter.out.model.UsuarioAdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioAdminJpaRepository extends JpaRepository<UsuarioAdminEntity, Long> {
    Optional<UsuarioAdminEntity> findByEmailIgnoreCaseAndEnableTrue(String email);

    Optional<UsuarioAdminEntity> findByIdAndEnableTrue(Long id);
}
