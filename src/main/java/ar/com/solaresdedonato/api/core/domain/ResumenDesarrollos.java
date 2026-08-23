package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResumenDesarrollos {
    private final long total;
    private final long enVenta;
    private final long preventa;
    private final long entregados;
}
