package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.command.ContenidoMediaCommand;
import ar.com.solaresdedonato.api.core.command.StampCommand;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.ports.service.FileStoragePort;
import ar.com.solaresdedonato.api.core.service.ContenidoMediaDomainService;
import ar.com.solaresdedonato.api.core.service.ValidadorArchivoImagen;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Alta de contenido vía multipart (archivo adjunto o link de video). Sigue siendo la
 * salida de emergencia si Google Drive o sus credenciales fallan — ver
 * {@link ImportarContenidoDesdeDrive} para el camino de importación desde Drive, que
 * comparte {@link ContenidoMediaDomainService} pero no este use case: los modos de
 * falla de cada origen son distintos (parseo de multipart vs 403/404/timeout de Drive),
 * y HEXAGONAL exige un único {@code execute} público por use case.
 *
 * <p>El guardado en disco ({@code fileStoragePort.store}) ocurre FUERA de cualquier
 * transacción; si {@code persistir} falla después, se compensa borrando el archivo ya
 * escrito. Antes esto vivía todo adentro de un único {@code @Transactional}, y un
 * rollback dejaba el archivo huérfano en disco para siempre.
 */
@Service
@RequiredArgsConstructor
public class CrearContenido {

    private final FileStoragePort fileStoragePort;
    private final ContenidoMediaDomainService contenidoMediaDomainService;
    private final ValidadorArchivoImagen validadorArchivoImagen;

    public ContenidoMedia execute(ContenidoMediaCommand command, StampCommand stamp) {
        ContenidoMediaDomainService.validarDesarrolloRequerido(command.getCategoria(), command.getDesarrolloId());

        String archivoUrl = null;

        if ("foto".equals(command.getTipo())) {
            if (command.getArchivoBytes() == null || command.getArchivoBytes().length == 0) {
                throw new BadRequestException(
                        "El archivo es requerido para contenido de tipo foto",
                        ErrorCodes.CONTENIDO_ARCHIVO_REQUERIDO,
                        Map.of()
                );
            }
            validadorArchivoImagen.validar(
                    command.getArchivoNombreOriginal(), command.getArchivoContentType(), command.getArchivoBytes());

            String subfolder = command.getDesarrolloId() != null ? "desarrollo-" + command.getDesarrolloId() : "institucional";
            archivoUrl = fileStoragePort.store(command.getArchivoBytes(), command.getArchivoNombreOriginal(), subfolder);
        } else if ("video".equals(command.getTipo()) &&
                (command.getVideoUrl() == null || command.getVideoUrl().isBlank())) {
            throw new BadRequestException(
                    "La URL del video es requerida para contenido de tipo video",
                    ErrorCodes.CONTENIDO_VIDEO_URL_REQUERIDO,
                    Map.of()
            );
        }

        boolean esPortada = ContenidoMediaDomainService.resolverEsPortada(
                command.getDesarrolloId(), command.getTipo(), command.isEsPortada());

        ContenidoMedia domain = ContenidoMedia.builder()
                .desarrolloId(command.getDesarrolloId())
                .tipo(command.getTipo())
                .titulo(command.getTitulo())
                .categoria(command.getCategoria())
                .descripcion(command.getDescripcion())
                .archivoUrl(archivoUrl)
                .videoUrl("video".equals(command.getTipo()) ? command.getVideoUrl() : null)
                .esPortada(esPortada)
                .orden(contenidoMediaDomainService.resolverOrden(command.getCategoria()))
                .enable(true)
                .stampApp(stamp.getStampApp())
                .stampUser(stamp.getStampUser())
                .stampDate(stamp.getStampDate())
                .build();

        if (archivoUrl == null) {
            return contenidoMediaDomainService.persistir(domain);
        }
        try {
            return contenidoMediaDomainService.persistir(domain);
        } catch (RuntimeException e) {
            fileStoragePort.delete(archivoUrl);
            throw e;
        }
    }
}
