package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.domain.DesarrolloFeature;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesarrolloFeatureRequest {

    @NotBlank(message = "La clave de la feature es requerida")
    private String clave;

    @NotBlank(message = "El título de la feature es requerido")
    private String titulo;

    @NotBlank(message = "El texto de la feature es requerido")
    private String texto;

    public DesarrolloFeature toDomain() {
        return DesarrolloFeature.builder()
                .clave(this.clave)
                .titulo(this.titulo)
                .texto(this.texto)
                .build();
    }
}
