package ar.com.solaresdedonato.api.adapter.out.repository;

import ar.com.solaresdedonato.api.adapter.out.jpa.DesarrolloJpaRepository;
import ar.com.solaresdedonato.api.adapter.out.mapper.DesarrolloMapper;
import ar.com.solaresdedonato.api.adapter.out.model.DesarrolloEntity;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.domain.ResumenDesarrollos;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DesarrolloRepository implements DesarrolloRepositoryPort {

    private final DesarrolloJpaRepository jpaRepository;
    private final DesarrolloMapper mapper;

    @Override
    public PageResult<Desarrollo> findPublicados(String estado, String zona, PageQuery pageQuery) {
        Page<DesarrolloEntity> page = jpaRepository.findPublicados(estado, zona, toPageable(pageQuery));
        return toPageResult(page);
    }

    @Override
    public PageResult<Desarrollo> findAdmin(String estado, String zona, Boolean publicado, PageQuery pageQuery) {
        Page<DesarrolloEntity> page = jpaRepository.findAdmin(estado, zona, publicado, toPageable(pageQuery));
        return toPageResult(page);
    }

    @Override
    public Optional<Desarrollo> findBySlugPublicado(String slug) {
        return jpaRepository.findBySlugAndEnableTrueAndPublicadoTrue(slug).map(mapper::toDomain);
    }

    @Override
    public Optional<Desarrollo> findById(Long id) {
        return jpaRepository.findByIdAndEnableTrue(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlugAndEnableTrue(slug);
    }

    @Override
    public boolean existsBySlugExcludingId(String slug, Long id) {
        return jpaRepository.existsBySlugAndEnableTrueAndIdNot(slug, id);
    }

    @Override
    public Desarrollo save(Desarrollo domain) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(domain)));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.findByIdAndEnableTrue(id).ifPresent(entity -> {
            entity.setEnable(false);
            jpaRepository.save(entity);
        });
    }

    @Override
    public ResumenDesarrollos resumen() {
        return ResumenDesarrollos.builder()
                .total(jpaRepository.countByEnableTrue())
                .enVenta(jpaRepository.countByEnableTrueAndEstado("en-venta"))
                .preventa(jpaRepository.countByEnableTrueAndEstado("preventa"))
                .entregados(jpaRepository.countByEnableTrueAndEstado("entregado"))
                .build();
    }

    private PageResult<Desarrollo> toPageResult(Page<DesarrolloEntity> page) {
        return PageResult.<Desarrollo>builder()
                .content(page.getContent().stream().map(mapper::toDomain).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private PageRequest toPageable(PageQuery pageQuery) {
        return PageRequest.of(pageQuery.getPage(), pageQuery.getSize());
    }
}
