package ar.com.solaresdedonato.api.adapter.out.service;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.ArchivoDescargado;
import ar.com.solaresdedonato.api.core.domain.ArchivoExterno;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.exception.BusinessException;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.CursorPageResult;
import ar.com.solaresdedonato.api.core.ports.service.ArchivoExternoPort;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Origen de ingesta de fotos y videos: Google Drive es una carpeta compartida de solo
 * lectura con un service account (nunca OAuth de usuario — ver plan de diseño: los
 * refresh tokens de OAuth se invalidan a los 7 días mientras la app esté en estado
 * "Testing", y la key del service account no expira). Al ser {@code drive.readonly},
 * esta integración físicamente no puede borrar ni modificar nada del Drive de Gastón.
 *
 * <p>Fotos y videos se tratan distinto río abajo (ver {@code ImportarContenidoDesdeDrive}):
 * las fotos se descargan y se re-alojan vía {@code FileStoragePort}; los videos NUNCA se
 * descargan acá, solo se enlazan con {@link #construirUrlReproduccion} — bajar un video
 * entero a memoria no es viable en un contenedor chico, y Drive ya sirve streaming.
 *
 * <p>Vive en {@code adapter/out/service/} y no en {@code adapter/out/storage/}: es la
 * convención de HEXAGONAL para adapters de servicios externos, y ese paquete ya está
 * excluido de JaCoCo (mockear la fluent API de {@code Drive} solo probaría que Mockito
 * funciona — la cobertura real de esta clase la da
 * {@code GoogleDriveArchivoExternoAdapterIT}, gateada a que exista una credencial real).
 */
@Slf4j
@Component
public class GoogleDriveArchivoExternoAdapter implements ArchivoExternoPort {

    private static final String CAMPOS_LISTADO =
            "nextPageToken, files(id,name,mimeType,size,createdTime,thumbnailLink,"
                    + "imageMediaMetadata/width,imageMediaMetadata/height,"
                    + "videoMediaMetadata/width,videoMediaMetadata/height)";
    private static final String CAMPOS_ARCHIVO =
            "id,name,mimeType,size,createdTime,thumbnailLink,"
                    + "imageMediaMetadata/width,imageMediaMetadata/height,"
                    + "videoMediaMetadata/width,videoMediaMetadata/height";

    private final Drive drive;
    private final HttpRequestFactory requestFactoryCredenciado;
    private final String folderId;

    public GoogleDriveArchivoExternoAdapter(
            @Value("${app.drive.credentials-json:}") String credentialsJson,
            @Value("${app.drive.folder-id}") String folderId,
            @Value("${app.drive.application-name}") String applicationName) {
        this.folderId = folderId;

        Drive driveTemp = null;
        HttpRequestFactory requestFactoryTemp = null;
        try {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleCredentials credentials = credencialesDesde(credentialsJson).createScoped(DriveScopes.DRIVE_READONLY);
            HttpCredentialsAdapter credentialsAdapter = new HttpCredentialsAdapter(credentials);

            driveTemp = new Drive.Builder(transport, jsonFactory, credentialsAdapter)
                    .setApplicationName(applicationName)
                    .build();
            requestFactoryTemp = transport.createRequestFactory(credentialsAdapter);
        } catch (Exception e) {
            // No hay credencial configurada todavía (recién clonado el repo, tests, CI sin
            // GOOGLE_DRIVE_CREDENTIALS_JSON) — Drive es una integración opcional, no tiene
            // que tirar abajo el arranque de TODA la app. Los métodos de esta clase fallan
            // limpio (BusinessException) recién si alguien efectivamente pega a /v1/drive/**.
            log.warn("Google Drive no está configurado ({}) — /v1/drive/** no va a funcionar hasta que se "
                    + "configuren GOOGLE_DRIVE_CREDENTIALS_JSON y GOOGLE_DRIVE_FOLDER_ID", e.getMessage());
        }
        this.drive = driveTemp;
        this.requestFactoryCredenciado = requestFactoryTemp;
    }

    private void verificarConfigurado() {
        if (drive == null) {
            throw new BusinessException(
                    "Google Drive no está configurado en este ambiente",
                    ErrorCodes.DRIVE_NO_CONFIGURADO,
                    Map.of());
        }
    }

    /** Si no hay credentials-json (típicamente en la notebook), cae a Application Default
     *  Credentials — permite usar GOOGLE_APPLICATION_CREDENTIALS localmente. */
    private GoogleCredentials credencialesDesde(String credentialsJson) throws IOException {
        if (credentialsJson == null || credentialsJson.isBlank()) {
            return GoogleCredentials.getApplicationDefault();
        }
        return GoogleCredentials.fromStream(new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public CursorPageResult<ArchivoExterno> listar(String nombreContiene, String pageToken, int size) {
        verificarConfigurado();
        try {
            Drive.Files.List request = drive.files().list()
                    .setQ(construirQuery(nombreContiene))
                    .setOrderBy("createdTime desc")
                    .setSupportsAllDrives(true)
                    .setIncludeItemsFromAllDrives(true)
                    .setFields(CAMPOS_LISTADO)
                    .setPageSize(size);
            if (pageToken != null && !pageToken.isBlank()) {
                request.setPageToken(pageToken);
            }

            FileList resultado = request.execute();
            List<ArchivoExterno> archivos = resultado.getFiles() == null
                    ? List.of()
                    : resultado.getFiles().stream().map(this::toDomain).toList();

            return new CursorPageResult<>(archivos, resultado.getNextPageToken());
        } catch (GoogleJsonResponseException e) {
            throw traducirError(e);
        } catch (IOException e) {
            throw new UncheckedIOException("Error listando archivos de Drive", e);
        }
    }

    @Override
    public Optional<ArchivoExterno> obtenerMetadata(String archivoId) {
        verificarConfigurado();
        try {
            File archivo = drive.files().get(archivoId)
                    .setSupportsAllDrives(true)
                    .setFields(CAMPOS_ARCHIVO)
                    .execute();
            return Optional.of(toDomain(archivo));
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) return Optional.empty();
            throw traducirError(e);
        } catch (IOException e) {
            throw new UncheckedIOException("Error obteniendo metadata de Drive", e);
        }
    }

    @Override
    public ArchivoDescargado descargar(String archivoId, long maxBytes) {
        verificarConfigurado();
        try {
            // Metadata primero: rechazar por tamaño ANTES de bajar un solo byte.
            File metadata = drive.files().get(archivoId)
                    .setSupportsAllDrives(true)
                    .setFields(CAMPOS_ARCHIVO)
                    .execute();

            Long tamanio = metadata.getSize();
            if (tamanio != null && tamanio > maxBytes) {
                throw new BadRequestException(
                        "El archivo supera el tamaño máximo permitido",
                        ErrorCodes.CONTENIDO_ARCHIVO_DEMASIADO_GRANDE,
                        Map.of("tamanioBytes", tamanio, "maximoBytes", maxBytes));
            }

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            drive.files().get(archivoId).setSupportsAllDrives(true).executeMediaAndDownloadTo(salida);

            return new ArchivoDescargado(archivoId, metadata.getName(), metadata.getMimeType(), salida.toByteArray());
        } catch (GoogleJsonResponseException e) {
            throw traducirError(e);
        } catch (IOException e) {
            throw new UncheckedIOException("Error descargando archivo de Drive", e);
        }
    }

    @Override
    public Optional<byte[]> descargarMiniatura(String archivoId) {
        // A diferencia de los otros métodos, acá no configurado se degrada igual que
        // cualquier otro fallo de miniatura: sin thumbnail, no sin la app entera.
        if (drive == null) return Optional.empty();
        try {
            File archivo = drive.files().get(archivoId)
                    .setSupportsAllDrives(true)
                    .setFields("thumbnailLink")
                    .execute();

            String thumbnailLink = archivo.getThumbnailLink();
            if (thumbnailLink == null) return Optional.empty();

            // El thumbnailLink de Drive requiere la credencial del service account — un
            // <img> del browser no puede mandarla, por eso este proxy baja los bytes acá.
            // Se pide TAL CUAL, sin tocar la URL: el formato real de thumbnailLink varía
            // (a veces sin "?", a veces ya con querystring) y concatenarle "&sz=..." a
            // ciegas arma una URL inválida — Google la rechaza con 400. El tamaño default
            // que devuelve Drive (~220px) ya alcanza para un tile de picker.
            HttpResponse response = requestFactoryCredenciado
                    .buildGetRequest(new GenericUrl(thumbnailLink))
                    .execute();
            try (InputStream contenido = response.getContent()) {
                return Optional.of(contenido.readAllBytes());
            }
        } catch (IOException e) {
            log.warn("No se pudo obtener la miniatura de Drive {}: {}", archivoId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public String construirUrlReproduccion(String archivoId) {
        // URL oficial de embed de Drive (la misma que arma "Compartir > Insertar
        // elemento"), reproduce inline en un <iframe> con el player propio de Drive.
        // No pega a la API, así que no necesita verificarConfigurado(): si el archivo no
        // está compartido públicamente, esta URL igual se arma, solo que no va a
        // reproducir para un visitante anónimo — es una limitación conocida (ver Javadoc
        // del port), no un error que este método pueda detectar de antemano.
        return "https://drive.google.com/file/d/" + archivoId + "/preview";
    }

    private String construirQuery(String nombreContiene) {
        StringBuilder q = new StringBuilder("'")
                .append(folderId)
                .append("' in parents and trashed = false and (mimeType contains 'image/' or mimeType contains 'video/')");
        // El filtro por mimeType excluye Google Docs/Sheets nativos, que no se pueden
        // bajar con alt=media (dan 403 fileNotDownloadable, solo sirven vía files.export).
        if (nombreContiene != null && !nombreContiene.isBlank()) {
            q.append(" and name contains '").append(escaparComillas(nombreContiene)).append("'");
        }
        return q.toString();
    }

    private String escaparComillas(String valor) {
        return valor.replace("'", "\\'");
    }

    private ArchivoExterno toDomain(File archivo) {
        Integer ancho = null;
        Integer alto = null;
        if (archivo.getImageMediaMetadata() != null) {
            ancho = archivo.getImageMediaMetadata().getWidth();
            alto = archivo.getImageMediaMetadata().getHeight();
        } else if (archivo.getVideoMediaMetadata() != null) {
            ancho = archivo.getVideoMediaMetadata().getWidth();
            alto = archivo.getVideoMediaMetadata().getHeight();
        }
        LocalDateTime fechaCreacion = archivo.getCreatedTime() != null
                ? Instant.ofEpochMilli(archivo.getCreatedTime().getValue()).atZone(ZoneId.systemDefault()).toLocalDateTime()
                : null;

        return new ArchivoExterno(
                archivo.getId(),
                archivo.getName(),
                archivo.getMimeType(),
                archivo.getSize(),
                ancho,
                alto,
                fechaCreacion,
                archivo.getThumbnailLink() != null);
    }

    private RuntimeException traducirError(GoogleJsonResponseException e) {
        int status = e.getStatusCode();
        if (status == 404) {
            return new NotFoundException(
                    "Archivo de Drive no encontrado", ErrorCodes.ARCHIVO_DRIVE_NO_ENCONTRADO, Map.of());
        }
        if (status == 403) {
            return new BusinessException(
                    "Sin permiso para acceder al archivo de Drive", ErrorCodes.DRIVE_SIN_PERMISO, Map.of());
        }
        return new UncheckedIOException("Error de Google Drive: " + e.getMessage(), new IOException(e));
    }
}
