package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.ArchivoExternoConEstado;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriveArchivoDto {
    private String id;
    private String nombre;
    private String mimeType;
    private Long tamanioBytes;
    private Integer anchoPx;
    private Integer altoPx;
    private LocalDateTime fechaCreacion;
    /** Nuestro proxy (GET /v1/drive/archivo/{id}/miniatura), NO el thumbnailLink de Drive
     *  — ese requiere la credencial del service account, un &lt;img&gt; no la tiene. */
    private String miniaturaUrl;
    private boolean yaImportado;
    private Long contenidoMediaId;

    public static DriveArchivoDto fromDomain(ArchivoExternoConEstado d) {
        var archivo = d.archivo();
        return DriveArchivoDto.builder()
                .id(archivo.id())
                .nombre(archivo.nombre())
                .mimeType(archivo.mimeType())
                .tamanioBytes(archivo.tamanioBytes())
                .anchoPx(archivo.anchoPx())
                .altoPx(archivo.altoPx())
                .fechaCreacion(archivo.fechaCreacion())
                .miniaturaUrl(archivo.tieneMiniatura() ? "/v1/drive/archivo/" + archivo.id() + "/miniatura" : null)
                .yaImportado(d.yaImportado())
                .contenidoMediaId(d.contenidoMediaId())
                .build();
    }
}
