package ar.com.solaresdedonato.api.adapter.out.jpa;

import ar.com.solaresdedonato.api.adapter.out.model.ConsultaContactoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultaContactoJpaRepository extends JpaRepository<ConsultaContactoEntity, Long> {
    Page<ConsultaContactoEntity> findByEnableTrueOrderByStampDateDesc(Pageable pageable);

    Optional<ConsultaContactoEntity> findByIdAndEnableTrue(Long id);
}
