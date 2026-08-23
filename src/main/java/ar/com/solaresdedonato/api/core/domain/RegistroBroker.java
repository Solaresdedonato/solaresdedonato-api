package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RegistroBroker {
    private final Long id;
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
    private final boolean enable;
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
