package ar.com.solaresdedonato.api.core.usecase.desarrollo;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ObtenerDesarrolloPorId {

    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public Desarrollo execute(Long id) {
        return desarrolloRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Desarrollo no encontrado",
                        ErrorCodes.DESARROLLO_NO_ENCONTRADO,
                        Map.of("id", id)
                ));
    }
}
