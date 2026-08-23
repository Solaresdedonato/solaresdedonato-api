package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class Desarrollo {
    private final Long id;
    private final String slug;
    private final String nombre;
    private final String zona;
    private final String direccion;
    private final String estado;
    private final String descripcion;
    private final List<DesarrolloFeature> features;
    private final DesarrolloCercanias cercanias;
    private final boolean instrumentoTokenizacion;
    private final boolean instrumentoRentaFija;
    private final boolean publicado;
    private final String imagenPortadaUrl;
    private final boolean enable;
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
