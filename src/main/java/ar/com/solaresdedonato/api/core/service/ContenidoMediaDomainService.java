package ar.com.solaresdedonato.api.core.service;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    /** 'hero' es la única categoría sin desarrollo (imágenes del carrusel de inicio,
     *  ver V6__contenido_media_categoria_hero.sql) — el resto sigue requiriéndolo. */
    public static void validarDesarrolloRequerido(String categoria, Long desarrolloId) {
        if (!"hero".equals(categoria) && desarrolloId == null) {
            throw new BadRequestException(
                    "El desarrollo es requerido para esta categoría de contenido",
                    ErrorCodes.CONTENIDO_DESARROLLO_REQUERIDO,
                    Map.of("categoria", categoria));
        }
    }

    /** El orden solo importa para 'hero' (define la secuencia del carrusel): una nueva
     *  se agrega al final. El resto de las categorías no lo usa todavía, entra en 0. */
    public short resolverOrden(String categoria) {
        if (!"hero".equals(categoria)) return 0;
        return (short) contenidoMediaRepositoryPort.countHeroHabilitadas();
    }
}
