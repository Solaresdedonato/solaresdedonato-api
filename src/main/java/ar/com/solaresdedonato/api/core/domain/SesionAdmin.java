package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SesionAdmin {
    private final String token;
    private final UsuarioAdmin usuario;
}
