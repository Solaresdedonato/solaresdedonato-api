package ar.com.solaresdedonato.api.core.usecase.desarrollo;

import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarDesarrollosPublicados {

    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public PageResult<Desarrollo> execute(String estado, String zona, PageQuery pageQuery) {
        return desarrolloRepositoryPort.findPublicados(estado, zona, pageQuery);
    }
}
