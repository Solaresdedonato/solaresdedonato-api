package ar.com.solaresdedonato.api.core.usecase.drive;

import ar.com.solaresdedonato.api.core.ports.service.ArchivoExternoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Pass-through de 5 líneas: existe solo porque el controller no puede llamar a un port
 * directamente. El motivo real de que esto sea un endpoint propio (en vez de que el
 * frontend use el {@code thumbnailLink} de Drive tal cual) es que ese link requiere la
 * credencial del service account — un {@code <img>} del browser no la tiene.
 */
@Service
@RequiredArgsConstructor
public class ObtenerMiniaturaArchivoDrive {

    private final ArchivoExternoPort archivoExternoPort;

    public Optional<byte[]> execute(String driveFileId) {
        return archivoExternoPort.descargarMiniatura(driveFileId);
    }
}
