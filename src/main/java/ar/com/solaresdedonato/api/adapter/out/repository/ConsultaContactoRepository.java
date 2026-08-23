package ar.com.solaresdedonato.api.adapter.out.repository;

import ar.com.solaresdedonato.api.adapter.out.jpa.ConsultaContactoJpaRepository;
import ar.com.solaresdedonato.api.adapter.out.mapper.ConsultaContactoMapper;
import ar.com.solaresdedonato.api.adapter.out.model.ConsultaContactoEntity;
import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.ConsultaContactoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConsultaContactoRepository implements ConsultaContactoRepositoryPort {

    private final ConsultaContactoJpaRepository jpaRepository;
    private final ConsultaContactoMapper mapper;

    @Override
    public PageResult<ConsultaContacto> findAll(PageQuery pageQuery) {
        Page<ConsultaContactoEntity> page = jpaRepository.findByEnableTrueOrderByStampDateDesc(
                PageRequest.of(pageQuery.getPage(), pageQuery.getSize()));
        return PageResult.<ConsultaContacto>builder()
                .content(page.getContent().stream().map(mapper::toDomain).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public Optional<ConsultaContacto> findById(Long id) {
        return jpaRepository.findByIdAndEnableTrue(id).map(mapper::toDomain);
    }

    @Override
    public ConsultaContacto save(ConsultaContacto domain) {
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
