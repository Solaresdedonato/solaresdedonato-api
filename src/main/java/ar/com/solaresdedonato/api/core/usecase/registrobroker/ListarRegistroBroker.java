package ar.com.solaresdedonato.api.core.usecase.registrobroker;

import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.RegistroBrokerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarRegistroBroker {

    private final RegistroBrokerRepositoryPort registroBrokerRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public PageResult<RegistroBroker> execute(PageQuery pageQuery) {
        return registroBrokerRepositoryPort.findAll(pageQuery);
    }
}
