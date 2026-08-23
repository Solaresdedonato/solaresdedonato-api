package ar.com.solaresdedonato.api.core.usecase.consultacontacto;

import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.ConsultaContactoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarConsultaContacto {

    private final ConsultaContactoRepositoryPort consultaContactoRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public PageResult<ConsultaContacto> execute(PageQuery pageQuery) {
        return consultaContactoRepositoryPort.findAll(pageQuery);
    }
}
