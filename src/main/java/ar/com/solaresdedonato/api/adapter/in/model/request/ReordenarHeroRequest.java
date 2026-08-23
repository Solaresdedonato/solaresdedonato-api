package ar.com.solaresdedonato.api.adapter.in.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReordenarHeroRequest {

    @NotEmpty(message = "La lista de ids es requerida")
    private List<Long> ids;
}
