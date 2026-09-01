package ar.com.solaresdedonato.api.adapter.out.repository;

import ar.com.solaresdedonato.api.adapter.out.jpa.ContenidoMediaJpaRepository;
import ar.com.solaresdedonato.api.adapter.out.mapper.ContenidoMediaMapper;
import ar.com.solaresdedonato.api.adapter.out.model.ContenidoMediaEntity;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ContenidoMediaRepository implements ContenidoMediaRepositoryPort {

    private final ContenidoMediaJpaRepository jpaRepository;
    private final ContenidoMediaMapper mapper;

    @Override
    public List<ContenidoMedia> findByDesarrolloId(Long desarrolloId) {
        return jpaRepository.findByDesarrolloId(desarrolloId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<ContenidoMedia> findHeroHabilitadas() {
        return jpaRepository.findHeroHabilitadas().stream().map(mapper::toDomain).toList();
    }

    @Override
    public long countHeroHabilitadas() {
        return jpaRepository.countByCategoriaAndEnableTrue("hero");
    }

    @Override
    public PageResult<ContenidoMedia> findByFiltro(Long desarrolloId, String tipo, String categoria, PageQuery pageQuery) {
        Page<ContenidoMediaEntity> page = jpaRepository.findByFiltro(
                desarrolloId, tipo, categoria, PageRequest.of(pageQuery.getPage(), pageQuery.getSize()));
        return PageResult.<ContenidoMedia>builder()
                .content(page.getContent().stream().map(mapper::toDomain).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public Optional<ContenidoMedia> findById(Long id) {
        return jpaRepository.findByIdAndEnableTrue(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ContenidoMedia> findPortadaByDesarrolloId(Long desarrolloId) {
        return jpaRepository.findPortadaByDesarrolloId(desarrolloId).map(mapper::toDomain);
    }

    @Override
    public Optional<ContenidoMedia> findByOrigenDriveFileIdAndDesarrolloIdAndCategoria(
            String driveFileId, Long desarrolloId, String categoria) {
        return jpaRepository
                .findByOrigenDriveFileIdAndDesarrolloIdAndCategoriaAndEnableTrue(driveFileId, desarrolloId, categoria)
                .map(mapper::toDomain);
    }

    @Override
    public Map<String, Long> findContenidoMediaIdsPorDriveFileId(List<String> driveFileIds) {
        if (driveFileIds.isEmpty()) return Map.of();
        // Un mismo drive_file_id puede tener más de una fila desde que se permite
        // reusar la misma foto en varios destinos (ver V7__origen_drive_unico_por_destino.sql)
        // -- Collectors.toMap de 2 argumentos tira IllegalStateException ante una clave
        // repetida. El uso de este mapa es puramente informativo (badge "yaImportado" del
        // picker), así que ante duplicados alcanza con quedarse con cualquiera; se toma el
        // de id más alto (la importación más reciente) de forma determinística.
        return jpaRepository.findIdsPorOrigenDriveFileId(driveFileIds).stream()
                .collect(Collectors.toMap(
                        fila -> (String) fila[0],
                        fila -> (Long) fila[1],
                        (idExistente, idNuevo) -> Math.max(idExistente, idNuevo)));
    }

    @Override
    public ContenidoMedia save(ContenidoMedia domain) {
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
