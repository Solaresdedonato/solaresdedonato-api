package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EliminarContenido {

    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;
    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void execute(Long id) {
        ContenidoMedia contenido = contenidoMediaRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Contenido no encontrado",
                        ErrorCodes.CONTENIDO_MEDIA_NO_ENCONTRADO,
                        Map.of("id", id)
                ));

        // Baja lógica: el archivo físico NO se borra. Antes sí se borraba acá, pero
        // ContenidoMediaRepository.deleteById solo hace enable=false — la fila sobrevivía
        // apuntando a un archivo que ya no existía, rompiendo la premisa de soft-delete
        // del resto del esquema. Si algún día hace falta reclamar espacio en disco, que
        // lo haga un sweeper con ventana de gracia (enable=false AND stamp_date < hoy - 90d),
        // no este use case.
        contenidoMediaRepositoryPort.deleteById(id);

        if (contenido.isEsPortada() && contenido.getDesarrolloId() != null) {
            desarrolloRepositoryPort.findById(contenido.getDesarrolloId())
                    .ifPresent(this::limpiarPortada);
        }
    }

    private void limpiarPortada(Desarrollo desarrollo) {
        desarrolloRepositoryPort.save(desarrollo.toBuilder().imagenPortadaUrl(null).build());
    }
}
