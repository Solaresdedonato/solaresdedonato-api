package ar.com.solaresdedonato.api.core.command;

import ar.com.solaresdedonato.api.core.domain.DesarrolloCercanias;
import ar.com.solaresdedonato.api.core.domain.DesarrolloFeature;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DesarrolloCommand {
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
    private final boolean publicar;
    private final String showroomVirtualUrl;
    private final String brochurePlanosUrl;
    private final String avanceObraUrl;
    private final String solicitarInformacionUrl;
}
