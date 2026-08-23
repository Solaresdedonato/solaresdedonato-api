package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.ports.PageResult;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarContenido {

    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public PageResult<ContenidoMedia> execute(Long desarrolloId, String tipo, String categoria, PageQuery pageQuery) {
        return contenidoMediaRepositoryPort.findByFiltro(desarrolloId, tipo, categoria, pageQuery);
    }
}
