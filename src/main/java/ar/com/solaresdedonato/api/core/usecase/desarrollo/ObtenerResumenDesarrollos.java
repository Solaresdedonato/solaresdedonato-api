package ar.com.solaresdedonato.api.core.usecase.desarrollo;

import ar.com.solaresdedonato.api.core.domain.ResumenDesarrollos;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ObtenerResumenDesarrollos {

    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public ResumenDesarrollos execute() {
        return desarrolloRepositoryPort.resumen();
    }
}
