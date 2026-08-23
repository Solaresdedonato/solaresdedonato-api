package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DesarrolloFeature {
    private final String clave;
    private final String titulo;
    private final String texto;
}
