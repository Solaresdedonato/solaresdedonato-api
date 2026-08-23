package ar.com.solaresdedonato.api.core.service;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lógica de persistencia común a los dos caminos de alta de {@link ContenidoMedia}
 * (multipart y descarga de Drive, ver {@code core/usecase/contenido/}): dueño de la
 * única frontera transaccional real de ambos, y de la invariante de portada única
 * por desarrollo.
 *
 * <p>Los use cases de alta guardan el archivo (I/O) ANTES de llamar acá y fuera de
 * cualquier transacción — si {@link #persistir} lanza, el use case es responsable de
 * compensar con {@code fileStoragePort.delete(...)}. Es una desviación deliberada de
 * "@Transactional solo en el execute del UseCase": antes el store() ocurría dentro de
 * la transacción del use case y un rollback posterior dejaba el archivo huérfano para
 * siempre. Bajar la frontera transaccional hasta acá permite esa compensación.
 */
@Service
@RequiredArgsConstructor
public class ContenidoMediaDomainService {

    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;
    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public ContenidoMedia persistir(ContenidoMedia contenido) {
        if (contenido.isEsPortada()) {
            contenidoMediaRepositoryPort.findPortadaByDesarrolloId(contenido.getDesarrolloId())
                    .ifPresent(anterior -> contenidoMediaRepositoryPort.save(anterior.toBuilder().esPortada(false).build()));
        }

        ContenidoMedia guardado = contenidoMediaRepositoryPort.save(contenido);

        if (contenido.isEsPortada()) {
            Desarrollo desarrollo = desarrolloRepositoryPort.findById(contenido.getDesarrolloId()).orElseThrow();
            desarrolloRepositoryPort.save(desarrollo.toBuilder().imagenPortadaUrl(contenido.getArchivoUrl()).build());
        }

        return guardado;
    }

    /** Regla compartida: solo una foto asociada a un desarrollo puede ser portada. */
    public static boolean resolverEsPortada(Long desarrolloId, String tipo, boolean solicitada) {
        return solicitada && "foto".equals(tipo) && desarrolloId != null;
    }
}
