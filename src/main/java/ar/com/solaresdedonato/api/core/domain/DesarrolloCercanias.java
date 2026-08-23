package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DesarrolloCercanias {
    private final List<String> educacion;
    private final List<String> transporte;
    private final List<String> comercios;
    private final List<String> salud;
}
