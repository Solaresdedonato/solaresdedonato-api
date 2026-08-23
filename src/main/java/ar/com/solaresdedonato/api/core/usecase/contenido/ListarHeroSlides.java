package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Imágenes del carrusel de inicio, en orden — lo consume el home público (sin login,
 *  ver SecurityConfig) y el backoffice para administrarlas. */
@Service
@RequiredArgsConstructor
public class ListarHeroSlides {

    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<ContenidoMedia> execute() {
        return contenidoMediaRepositoryPort.findHeroHabilitadas();
    }
}
