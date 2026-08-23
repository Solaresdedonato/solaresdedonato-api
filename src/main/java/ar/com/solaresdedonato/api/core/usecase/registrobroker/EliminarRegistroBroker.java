package ar.com.solaresdedonato.api.core.usecase.registrobroker;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.RegistroBrokerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EliminarRegistroBroker {

    private final RegistroBrokerRepositoryPort registroBrokerRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void execute(Long id) {
        registroBrokerRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Registro de broker no encontrado",
                        ErrorCodes.REGISTRO_BROKER_NO_ENCONTRADO,
                        Map.of("id", id)
                ));

        registroBrokerRepositoryPort.deleteById(id);
    }
}
