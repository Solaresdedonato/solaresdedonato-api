package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.ArchivoExternoConEstado;
import ar.com.solaresdedonato.api.core.ports.CursorPageResult;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Deliberadamente NO {@link PageDto}: Drive pagina por cursor (pageToken), no por
 * offset/total, así que forzar el shape compartido obligaría a inventar un total falso.
 */
@Data
@Builder
public class DriveArchivoPageDto {
    private List<DriveArchivoDto> archivos;
    private String nextPageToken;

    public static DriveArchivoPageDto fromCursorPageResult(CursorPageResult<ArchivoExternoConEstado> result) {
        return DriveArchivoPageDto.builder()
                .archivos(result.content().stream().map(DriveArchivoDto::fromDomain).toList())
                .nextPageToken(result.nextPageToken())
                .build();
    }
}
