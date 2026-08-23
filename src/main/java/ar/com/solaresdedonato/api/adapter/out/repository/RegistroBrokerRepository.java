package ar.com.solaresdedonato.api.adapter.out.repository;

import ar.com.solaresdedonato.api.adapter.out.jpa.RegistroBrokerJpaRepository;
import ar.com.solaresdedonato.api.adapter.out.mapper.RegistroBrokerMapper;
import ar.com.solaresdedonato.api.adapter.out.model.RegistroBrokerEntity;
import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.RegistroBrokerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegistroBrokerRepository implements RegistroBrokerRepositoryPort {

    private final RegistroBrokerJpaRepository jpaRepository;
    private final RegistroBrokerMapper mapper;

    @Override
    public PageResult<RegistroBroker> findAll(PageQuery pageQuery) {
        Page<RegistroBrokerEntity> page = jpaRepository.findByEnableTrueOrderByStampDateDesc(
                PageRequest.of(pageQuery.getPage(), pageQuery.getSize()));
        return PageResult.<RegistroBroker>builder()
                .content(page.getContent().stream().map(mapper::toDomain).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public Optional<RegistroBroker> findById(Long id) {
        return jpaRepository.findByIdAndEnableTrue(id).map(mapper::toDomain);
    }

    @Override
    public RegistroBroker save(RegistroBroker domain) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(domain)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.findByIdAndEnableTrue(id).ifPresent(entity -> {
            entity.setEnable(false);
            jpaRepository.save(entity);
        });
    }
}
