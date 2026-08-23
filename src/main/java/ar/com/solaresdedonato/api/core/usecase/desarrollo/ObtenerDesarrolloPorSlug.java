package ar.com.solaresdedonato.api.core.usecase.desarrollo;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.domain.DesarrolloDetalle;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/** Compone dos repository ports (Desarrollo + ContenidoMedia) para armar el read-model de detalle público. */
@Service
@RequiredArgsConstructor
public class ObtenerDesarrolloPorSlug {

    private final DesarrolloRepositoryPort desarrolloRepositoryPort;
    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public DesarrolloDetalle execute(String slug) {
        Desarrollo desarrollo = desarrolloRepositoryPort.findBySlugPublicado(slug)
                .orElseThrow(() -> new NotFoundException(
                        "Desarrollo no encontrado",
                        ErrorCodes.DESARROLLO_NO_ENCONTRADO,
                        Map.of("slug", slug)
                ));

        return DesarrolloDetalle.builder()
                .desarrollo(desarrollo)
                .galeria(contenidoMediaRepositoryPort.findByDesarrolloId(desarrollo.getId()))
                .build();
    }
}
