package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class UsuarioAdmin {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String nombre;
    private final String rol;
    private final LocalDateTime ultimoLoginDate;
    private final boolean enable;
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
