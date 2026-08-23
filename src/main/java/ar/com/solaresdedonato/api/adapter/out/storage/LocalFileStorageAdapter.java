package ar.com.solaresdedonato.api.adapter.out.storage;

import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.ports.service.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

/**
 * Guarda archivos en disco local (sin AWS/S3, ver plan de diseño). Spring sirve esa misma
 * carpeta como estáticos vía WebConfig, mapeando app.storage.local.base-dir a /media/**.
 *
 * <p>Devuelve un path RELATIVO ("/media/desarrollo-1/x.jpg"), no una URL absoluta con host:
 * desacopla las filas de contenido_media del dominio donde corra la API — cambiarlo (o
 * migrar de host) no exige tocar ni una fila existente. El frontend antepone su propio
 * VITE_API_URL antes de usarla.
 */
@Slf4j
@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    private static final int MAX_LARGO_NOMBRE_BASE = 60;

    private final Path baseDir;

    public LocalFileStorageAdapter(@Value("${app.storage.local.base-dir}") String baseDir) {
        this.baseDir = Path.of(baseDir).toAbsolutePath().normalize();
    }

    @Override
    public String store(byte[] bytes, String filename, String subfolder) {
        try {
            Path folder = baseDir.resolve(subfolder).normalize();
            if (!folder.startsWith(baseDir)) {
                throw new BadRequestException("Nombre de carpeta inválido", ErrorCodes.ERROR_VALIDACION, Map.of("subfolder", subfolder));
            }
            Files.createDirectories(folder);

            String uniqueName = UUID.randomUUID() + "-" + sanitize(filename);
            Path destino = folder.resolve(uniqueName);
            Files.write(destino, bytes);

            String relativePath = subfolder + "/" + uniqueName;
            return "/media/" + relativePath;
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo", e);
        }
    }

    @Override
    public void delete(String url) {
        if (url == null) return;

        String marker = "/media/";
        int idx = url.indexOf(marker);
        if (idx < 0) return;

        Path target = baseDir.resolve(url.substring(idx + marker.length())).normalize();
        if (!target.startsWith(baseDir)) return;

        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            log.warn("No se pudo borrar el archivo {}: {}", target, e.getMessage());
        }
    }

    /**
     * Sanea el nombre original y lo trunca a {@value #MAX_LARGO_NOMBRE_BASE} caracteres
     * (conservando la extensión) antes de anteponerle el UUID. Un nombre de archivo de
     * varios cientos de caracteres — perfectamente posible en un archivo bajado de
     * WhatsApp o de Drive — sin este tope rompería el INSERT contra archivo_url.
     */
    private String sanitize(String filename) {
        if (filename == null || filename.isBlank()) return "archivo";

        String limpio = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        int punto = limpio.lastIndexOf('.');
        String extension = (punto > 0 && limpio.length() - punto <= 6) ? limpio.substring(punto) : "";
        String base = (punto > 0) ? limpio.substring(0, punto) : limpio;

        if (base.length() > MAX_LARGO_NOMBRE_BASE) {
            base = base.substring(0, MAX_LARGO_NOMBRE_BASE);
        }
        return base + extension;
    }
}
