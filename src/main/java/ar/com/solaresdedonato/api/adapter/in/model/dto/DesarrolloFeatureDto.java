package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.DesarrolloFeature;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DesarrolloFeatureDto {
    private String clave;
    private String titulo;
    private String texto;

    public static DesarrolloFeatureDto fromDomain(DesarrolloFeature d) {
        return DesarrolloFeatureDto.builder()
                .clave(d.getClave())
                .titulo(d.getTitulo())
                .texto(d.getTexto())
                .build();
    }
}
