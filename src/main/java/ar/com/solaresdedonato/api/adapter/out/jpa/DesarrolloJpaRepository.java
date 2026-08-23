package ar.com.solaresdedonato.api.adapter.out.jpa;

import ar.com.solaresdedonato.api.adapter.out.model.DesarrolloEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DesarrolloJpaRepository extends JpaRepository<DesarrolloEntity, Long> {

    @Query("""
            SELECT d FROM DesarrolloEntity d
            WHERE d.enable = true AND d.publicado = true
              AND (:estado IS NULL OR d.estado = :estado)
              AND (:zona IS NULL OR LOWER(d.zona) LIKE LOWER(CONCAT('%', CAST(:zona AS string), '%')))
            """)
    Page<DesarrolloEntity> findPublicados(@Param("estado") String estado, @Param("zona") String zona, Pageable pageable);

    @Query("""
            SELECT d FROM DesarrolloEntity d
            WHERE d.enable = true
              AND (:estado IS NULL OR d.estado = :estado)
              AND (:zona IS NULL OR LOWER(d.zona) LIKE LOWER(CONCAT('%', CAST(:zona AS string), '%')))
              AND (:publicado IS NULL OR d.publicado = :publicado)
            """)
    Page<DesarrolloEntity> findAdmin(@Param("estado") String estado, @Param("zona") String zona,
                                      @Param("publicado") Boolean publicado, Pageable pageable);

    Optional<DesarrolloEntity> findBySlugAndEnableTrueAndPublicadoTrue(String slug);

    Optional<DesarrolloEntity> findByIdAndEnableTrue(Long id);

    boolean existsBySlugAndEnableTrue(String slug);

    boolean existsBySlugAndEnableTrueAndIdNot(String slug, Long id);

    long countByEnableTrue();

    long countByEnableTrueAndEstado(String estado);
}
