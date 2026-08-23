package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultaContactoCommand {
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String telefono;
    private final String proyectoInteres;
    private final String mensaje;
}
