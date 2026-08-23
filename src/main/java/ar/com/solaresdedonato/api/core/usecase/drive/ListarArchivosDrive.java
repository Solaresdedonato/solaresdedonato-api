package ar.com.solaresdedonato.api.core.usecase.drive;

import ar.com.solaresdedonato.api.core.domain.ArchivoExterno;
import ar.com.solaresdedonato.api.core.domain.ArchivoExternoConEstado;
import ar.com.solaresdedonato.api.core.ports.CursorPageResult;
import ar.com.solaresdedonato.api.core.ports.repository.ContenidoMediaRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.service.ArchivoExternoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ListarArchivosDrive {

    private final ArchivoExternoPort archivoExternoPort;
    private final ContenidoMediaRepositoryPort contenidoMediaRepositoryPort;

    public CursorPageResult<ArchivoExternoConEstado> execute(String nombreContiene, String pageToken, int size) {
        CursorPageResult<ArchivoExterno> pagina = archivoExternoPort.listar(nombreContiene, pageToken, size);

        List<String> ids = pagina.content().stream().map(ArchivoExterno::id).toList();
        // Una sola consulta para toda la página, no una por archivo.
        Map<String, Long> importados = contenidoMediaRepositoryPort.findContenidoMediaIdsPorDriveFileId(ids);

        List<ArchivoExternoConEstado> conEstado = pagina.content().stream()
                .map(archivo -> new ArchivoExternoConEstado(
                        archivo, importados.containsKey(archivo.id()), importados.get(archivo.id())))
                .toList();

        return new CursorPageResult<>(conEstado, pagina.nextPageToken());
    }
}
