package ar.com.solaresdedonato.api.core.usecase.desarrollo;

import ar.com.solaresdedonato.api.core.command.DesarrolloCommand;
import ar.com.solaresdedonato.api.core.command.StampCommand;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.exception.ConflictException;
import ar.com.solaresdedonato.api.core.ports.repository.DesarrolloRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CrearDesarrollo {

    private static final java.util.Set<String> SLUGS_RESERVADOS = java.util.Set.of("admin", "resumen");

    private final DesarrolloRepositoryPort desarrolloRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public Desarrollo execute(DesarrolloCommand command, StampCommand stamp) {
        if (SLUGS_RESERVADOS.contains(command.getSlug())) {
            throw new BadRequestException(
                    "El slug '" + command.getSlug() + "' está reservado",
                    ErrorCodes.DESARROLLO_SLUG_RESERVADO,
                    Map.of("slug", command.getSlug())
            );
        }
        if (desarrolloRepositoryPort.existsBySlug(command.getSlug())) {
            throw new ConflictException(
                    "Ya existe un desarrollo con ese slug",
                    ErrorCodes.DESARROLLO_SLUG_YA_EXISTE,
                    Map.of("slug", command.getSlug())
            );
        }

        Desarrollo domain = Desarrollo.builder()
                .slug(command.getSlug())
                .nombre(command.getNombre())
                .zona(command.getZona())
                .direccion(command.getDireccion())
                .estado(command.getEstado())
                .descripcion(command.getDescripcion())
                .features(command.getFeatures())
                .cercanias(command.getCercanias())
                .instrumentoTokenizacion(command.isInstrumentoTokenizacion())
                .instrumentoRentaFija(command.isInstrumentoRentaFija())
                .publicado(command.isPublicar())
                .imagenPortadaUrl(null)
                .showroomVirtualUrl(command.getShowroomVirtualUrl())
                .brochurePlanosUrl(command.getBrochurePlanosUrl())
                .avanceObraUrl(command.getAvanceObraUrl())
                .solicitarInformacionUrl(command.getSolicitarInformacionUrl())
                .enable(true)
                .stampApp(stamp.getStampApp())
                .stampUser(stamp.getStampUser())
                .stampDate(stamp.getStampDate())
                .build();

        return desarrolloRepositoryPort.save(domain);
    }
}
