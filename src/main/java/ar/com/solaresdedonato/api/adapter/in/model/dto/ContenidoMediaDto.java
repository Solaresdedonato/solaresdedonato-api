package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContenidoMediaDto {
    private Long id;
    private Long desarrolloId;
    private String desarrolloNombre;
    private String tipo;
    private String titulo;
    private String categoria;
    private String descripcion;
    private String archivoUrl;
    private String videoUrl;
    private boolean esPortada;
    private int orden;

    public static ContenidoMediaDto fromDomain(ContenidoMedia d) {
        return ContenidoMediaDto.builder()
                .id(d.getId())
                .desarrolloId(d.getDesarrolloId())
                .desarrolloNombre(d.getDesarrolloNombre())
                .tipo(d.getTipo())
                .titulo(d.getTitulo())
                .categoria(d.getCategoria())
                .descripcion(d.getDescripcion())
                .archivoUrl(d.getArchivoUrl())
                .videoUrl(d.getVideoUrl())
                .esPortada(d.isEsPortada())
                .orden(d.getOrden())
                .build();
    }
}
