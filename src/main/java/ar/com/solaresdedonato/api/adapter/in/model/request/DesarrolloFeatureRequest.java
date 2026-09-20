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

    /** Opcional: el desarrollo se puede crear con solo los datos generales. Se guarda como
     *  "" (la tabla exige siempre 4 features, ver ck_desarrollo_features_shape) y la ficha
     *  pública omite las que no tienen texto. */
    private String texto;

    public DesarrolloFeature toDomain() {
        return DesarrolloFeature.builder()
                .clave(this.clave)
                .titulo(this.titulo)
                .texto(this.texto == null ? "" : this.texto)
                .build();
    }
}
