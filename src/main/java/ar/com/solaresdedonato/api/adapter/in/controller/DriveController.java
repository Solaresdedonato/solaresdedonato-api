package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.DriveArchivoPageDto;
import ar.com.solaresdedonato.api.core.usecase.drive.ListarArchivosDrive;
import ar.com.solaresdedonato.api.core.usecase.drive.ObtenerMiniaturaArchivoDrive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Listado y miniaturas de la carpeta de Drive configurada como origen de ingesta
 * (ver plan de diseño) — no es "listar contenido" ({@link ContenidoController}), son
 * entidades distintas con DTOs distintos. El folderId es server-side y fijo: este
 * controller no acepta uno del cliente, o la API se vuelve un proxy de lectura de
 * cualquier cosa que el service account tenga compartida.
 */
@RestController
@RequestMapping("/v1/drive")
@RequiredArgsConstructor
public class DriveController {

    private final ListarArchivosDrive listarArchivosDrive;
    private final ObtenerMiniaturaArchivoDrive obtenerMiniaturaArchivoDrive;

    @GetMapping("/archivo")
    public ResponseEntity<DriveArchivoPageDto> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String pageToken,
            @RequestParam(defaultValue = "100") int size) {
        var resultado = listarArchivosDrive.execute(nombre, pageToken, size);
        return ResponseEntity.ok(DriveArchivoPageDto.fromCursorPageResult(resultado));
    }

    @GetMapping("/archivo/{driveFileId}/miniatura")
    public ResponseEntity<byte[]> miniatura(@PathVariable String driveFileId) {
        return obtenerMiniaturaArchivoDrive.execute(driveFileId)
                .map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePrivate())
                        .body(bytes))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
