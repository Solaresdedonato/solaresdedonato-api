package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RegistroBrokerCommand {
    private final String nombre;
    private final String email;
    private final String telefono;
    private final String inmobiliaria;
    private final String matricula;
    private final String experiencia;
    private final String zonaOperacion;
    private final List<String> tipoOperaciones;
    private final String operacionesCerradas;
    private final String mensaje;
}
