package ar.com.solaresdedonato.api.core.usecase.contenido;

import ar.com.solaresdedonato.api.core.command.ImportarContenidoDriveCommand;
import ar.com.solaresdedonato.api.core.command.StampCommand;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ArchivoDescargado;
import ar.com.solaresdedonato.api.core.domain.ArchivoExterno;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.exception.ConflictException;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.service.ArchivoExternoPort;
import ar.com.solaresdedonato.api.core.ports.service.FileStoragePort;
import ar.com.solaresdedonato.api.core.service.ContenidoMediaDomainService;
import ar.com.solaresdedonato.api.core.service.ValidadorArchivoImagen;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Importa un archivo ya subido a la carpeta de Google Drive. El {@code tipo} lo decide
 * ESTE use case a partir del mimeType real que reporta Drive — nunca lo que mande el
 * cliente — para que no exista forma de importar un archivo pretendiendo que es otra cosa:
 * <ul>
 *   <li><b>foto</b>: se descarga una vez y se entrega al {@link FileStoragePort} existente,
 *       igual que una subida multipart — misma URL relativa, mismo /media/**.</li>
 *   <li><b>video</b>: NUNCA se descarga. Se enlaza directo a Drive vía
 *       {@link ArchivoExternoPort#construirUrlReproduccion} — bajar un video entero a
 *       memoria no es viable acá, y requiere que el archivo esté compartido como
 *       "cualquiera con el link" en Drive para que se vea en el sitio público (ver
 *       Javadoc del port).</li>
 * </ul>
 *
 * <p>No reutiliza {@link CrearContenido}: HEXAGONAL exige un único {@code execute}
 * público por use case, y los modos de falla de cada origen son distintos (multipart
 * falla parseando el request; Drive falla con 403/404/timeout/tamaño remoto) — mezclarlos
 * vuelve combinatoria la matriz de tests. Comparten {@link ContenidoMediaDomainService}.
 */
@Service
@RequiredArgsConstructor
public class ImportarContenidoDesdeDrive {

    private static final String TIPO_FOTO = "foto";
    private static final String TIPO_VIDEO = "video";

    private final ArchivoExternoPort archivoExternoPort;
    private final FileStoragePort fileStoragePort;
    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;
    private final ContenidoMediaDomainService contenidoMediaDomainService;
    private final ValidadorArchivoImagen validadorArchivoImagen;

    public ContenidoMedia execute(ImportarContenidoDriveCommand command, StampCommand stamp) {
        contenidoMediaRepositoryPort.findByOrigenDriveFileId(command.getDriveFileId())
                .ifPresent(existente -> {
                    throw new ConflictException(
                            "Este archivo de Drive ya fue importado a la biblioteca",
                            ErrorCodes.CONTENIDO_DRIVE_YA_IMPORTADO,
                            Map.of("contenidoMediaId", existente.getId()));
                });

        ArchivoExterno metadata = archivoExternoPort.obtenerMetadata(command.getDriveFileId())
                .orElseThrow(() -> new NotFoundException(
                        "El archivo ya no está disponible en Drive",
                        ErrorCodes.ARCHIVO_DRIVE_NO_ENCONTRADO,
                        Map.of("driveFileId", command.getDriveFileId())));

        String tipo = resolverTipo(metadata.mimeType());

        String archivoUrl = null;
        String videoUrl = null;

        if (TIPO_FOTO.equals(tipo)) {
            // Rechazo por tamaño ANTES de bajar un solo byte: multipart.max-file-size no
            // aplica acá, y un archivo enorme en la carpeta no debería tirar el contenedor.
            validadorArchivoImagen.validarTamanioDeclarado(metadata.tamanioBytes());

            ArchivoDescargado descargado = archivoExternoPort.descargar(command.getDriveFileId(), validadorArchivoImagen.maxBytes());
            validadorArchivoImagen.validar(descargado.nombre(), descargado.mimeType(), descargado.contenido());

            String subfolder = command.getDesarrolloId() != null ? "desarrollo-" + command.getDesarrolloId() : "institucional";
            archivoUrl = fileStoragePort.store(descargado.contenido(), descargado.nombre(), subfolder);
        } else {
            // video: nada se descarga ni se guarda en FileStoragePort, solo se linkea.
            videoUrl = archivoExternoPort.construirUrlReproduccion(command.getDriveFileId());
        }

        boolean esPortada = ContenidoMediaDomainService.resolverEsPortada(
                command.getDesarrolloId(), tipo, command.isEsPortada());

        ContenidoMedia domain = ContenidoMedia.builder()
                .desarrolloId(command.getDesarrolloId())
                .tipo(tipo)
                .titulo(command.getTitulo())
                .categoria(command.getCategoria())
                .descripcion(command.getDescripcion())
                .archivoUrl(archivoUrl)
                .videoUrl(videoUrl)
                .origenDriveFileId(command.getDriveFileId())
                .esPortada(esPortada)
                .orden((short) 0)
                .enable(true)
                .stampApp(stamp.getStampApp())
                .stampUser(stamp.getStampUser())
                .stampDate(stamp.getStampDate())
                .build();

        if (archivoUrl == null) {
            // Camino video: nada que compensar si persistir() falla, no se escribió nada.
            return contenidoMediaDomainService.persistir(domain);
        }
        try {
            return contenidoMediaDomainService.persistir(domain);
        } catch (RuntimeException e) {
            fileStoragePort.delete(archivoUrl);
            throw e;
        }
    }

    private String resolverTipo(String mimeType) {
        if (mimeType != null && mimeType.startsWith("image/")) return TIPO_FOTO;
        if (mimeType != null && mimeType.startsWith("video/")) return TIPO_VIDEO;
        throw new BadRequestException(
                "El archivo de Drive no es una foto ni un video soportado",
                ErrorCodes.CONTENIDO_TIPO_ARCHIVO_NO_PERMITIDO,
                Map.of("mimeType", mimeType));
    }
}
