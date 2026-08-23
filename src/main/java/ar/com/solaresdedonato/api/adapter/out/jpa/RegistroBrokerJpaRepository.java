package ar.com.solaresdedonato.api.adapter.out.jpa;

import ar.com.solaresdedonato.api.adapter.out.model.RegistroBrokerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistroBrokerJpaRepository extends JpaRepository<RegistroBrokerEntity, Long> {
    Page<RegistroBrokerEntity> findByEnableTrueOrderByStampDateDesc(Pageable pageable);

    Optional<RegistroBrokerEntity> findByIdAndEnableTrue(Long id);
}
