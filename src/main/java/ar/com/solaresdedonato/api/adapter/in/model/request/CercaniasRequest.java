package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.domain.DesarrolloCercanias;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CercaniasRequest {

    @NotNull
    private List<String> educacion;

    @NotNull
    private List<String> transporte;

    @NotNull
    private List<String> comercios;

    @NotNull
    private List<String> salud;

    public DesarrolloCercanias toDomain() {
        return DesarrolloCercanias.builder()
                .educacion(this.educacion)
                .transporte(this.transporte)
                .comercios(this.comercios)
                .salud(this.salud)
                .build();
    }
}
