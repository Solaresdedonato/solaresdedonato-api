package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConsultaContacto {
    private final Long id;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final String telefono;
    private final String proyectoInteres;
    private final String mensaje;
    private final boolean enable;
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
