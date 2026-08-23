package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reordena el carrusel de inicio: recibe los ids de las imágenes 'hero' en el orden
 * deseado y reescribe {@code orden} = índice en la lista para cada una. Un id que no
 * exista o no sea de categoría 'hero' aborta todo el reordenamiento (transaccional).
 */
@Service
@RequiredArgsConstructor
public class ReordenarHeroSlides {

    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public List<ContenidoMedia> execute(List<Long> idsEnOrden) {
        List<ContenidoMedia> reordenadas = new ArrayList<>();
        for (int i = 0; i < idsEnOrden.size(); i++) {
            Long id = idsEnOrden.get(i);
            ContenidoMedia existente = contenidoMediaRepositoryPort.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            "Contenido no encontrado", ErrorCodes.CONTENIDO_MEDIA_NO_ENCONTRADO, Map.of("id", id)));
            if (!"hero".equals(existente.getCategoria())) {
                throw new BadRequestException(
                        "El id no corresponde a una imagen del carrusel de inicio",
                        ErrorCodes.CONTENIDO_HERO_ID_INVALIDO,
                        Map.of("id", id));
            }
            reordenadas.add(contenidoMediaRepositoryPort.save(existente.toBuilder().orden((short) i).build()));
        }
        return reordenadas;
    }
}
