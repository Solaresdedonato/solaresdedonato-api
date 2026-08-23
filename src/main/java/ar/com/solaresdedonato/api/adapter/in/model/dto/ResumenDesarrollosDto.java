package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.ResumenDesarrollos;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumenDesarrollosDto {
    private long total;
    private long enVenta;
    private long preventa;
    private long entregados;

    public static ResumenDesarrollosDto fromDomain(ResumenDesarrollos r) {
        return ResumenDesarrollosDto.builder()
                .total(r.getTotal())
                .enVenta(r.getEnVenta())
                .preventa(r.getPreventa())
                .entregados(r.getEntregados())
                .build();
    }
}
