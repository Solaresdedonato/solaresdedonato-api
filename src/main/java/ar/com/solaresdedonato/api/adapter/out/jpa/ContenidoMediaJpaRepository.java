package ar.com.solaresdedonato.api.adapter.out.jpa;

import ar.com.solaresdedonato.api.adapter.out.model.ContenidoMediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContenidoMediaJpaRepository extends JpaRepository<ContenidoMediaEntity, Long> {

    @Query("""
            SELECT c FROM ContenidoMediaEntity c LEFT JOIN FETCH c.desarrollo
            WHERE c.enable = true AND c.desarrollo.id = :desarrolloId
            ORDER BY c.orden ASC, c.id ASC
            """)
    List<ContenidoMediaEntity> findByDesarrolloId(@Param("desarrolloId") Long desarrolloId);

    @Query("""
            SELECT c FROM ContenidoMediaEntity c LEFT JOIN FETCH c.desarrollo
            WHERE c.enable = true
              AND (:desarrolloId IS NULL OR c.desarrollo.id = :desarrolloId)
              AND (:tipo IS NULL OR c.tipo = :tipo)
              AND (:categoria IS NULL OR c.categoria = :categoria)
            ORDER BY c.orden ASC, c.id ASC
            """)
    Page<ContenidoMediaEntity> findByFiltro(@Param("desarrolloId") Long desarrolloId, @Param("tipo") String tipo,
                                             @Param("categoria") String categoria, Pageable pageable);

    @Query("""
            SELECT c FROM ContenidoMediaEntity c
            WHERE c.enable = true AND c.categoria = 'hero'
            ORDER BY c.orden ASC, c.id ASC
            """)
    List<ContenidoMediaEntity> findHeroHabilitadas();

    long countByCategoriaAndEnableTrue(String categoria);

    @Query("SELECT c FROM ContenidoMediaEntity c LEFT JOIN FETCH c.desarrollo WHERE c.id = :id AND c.enable = true")
    Optional<ContenidoMediaEntity> findByIdAndEnableTrue(@Param("id") Long id);

    @Query("SELECT c FROM ContenidoMediaEntity c WHERE c.desarrollo.id = :desarrolloId AND c.esPortada = true AND c.enable = true")
    Optional<ContenidoMediaEntity> findPortadaByDesarrolloId(@Param("desarrolloId") Long desarrolloId);

    @Query("""
            SELECT c FROM ContenidoMediaEntity c LEFT JOIN FETCH c.desarrollo
            WHERE c.origenDriveFileId = :driveFileId
              AND c.categoria = :categoria
              AND c.enable = true
              AND ((:desarrolloId IS NULL AND c.desarrollo IS NULL) OR c.desarrollo.id = :desarrolloId)
            """)
    Optional<ContenidoMediaEntity> findByOrigenDriveFileIdAndDesarrolloIdAndCategoriaAndEnableTrue(
            @Param("driveFileId") String driveFileId, @Param("desarrolloId") Long desarrolloId,
            @Param("categoria") String categoria);

    @Query("SELECT c.origenDriveFileId, c.id FROM ContenidoMediaEntity c " +
            "WHERE c.origenDriveFileId IN :driveFileIds AND c.enable = true")
    List<Object[]> findIdsPorOrigenDriveFileId(@Param("driveFileIds") List<String> driveFileIds);
}
