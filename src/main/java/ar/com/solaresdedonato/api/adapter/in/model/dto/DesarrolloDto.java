package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesarrolloDto {
    private Long id;
    private String slug;
    private String nombre;
    private String zona;
    private String direccion;
    private String estado;
    private String descripcion;
    private List<DesarrolloFeatureDto> features;
    private Map<String, List<String>> cercanias;
    private boolean instrumentoTokenizacion;
    private boolean instrumentoRentaFija;
    private boolean publicado;
    private String imagenPortadaUrl;
    private String showroomVirtualUrl;
    private String brochurePlanosUrl;
    private String avanceObraUrl;
    private String solicitarInformacionUrl;
    /** Solo se completa en el detalle público por slug (GET /v1/desarrollo/{slug}). */
    private List<ContenidoMediaDto> galeria;

    public static DesarrolloDto fromDomain(Desarrollo d) {
        return DesarrolloDto.builder()
                .id(d.getId())
                .slug(d.getSlug())
                .nombre(d.getNombre())
                .zona(d.getZona())
                .direccion(d.getDireccion())
                .estado(d.getEstado())
                .descripcion(d.getDescripcion())
                .features(d.getFeatures().stream().map(DesarrolloFeatureDto::fromDomain).toList())
                .cercanias(Map.of(
                        "educacion", d.getCercanias().getEducacion(),
                        "transporte", d.getCercanias().getTransporte(),
                        "comercios", d.getCercanias().getComercios(),
                        "salud", d.getCercanias().getSalud()
                ))
                .instrumentoTokenizacion(d.isInstrumentoTokenizacion())
                .instrumentoRentaFija(d.isInstrumentoRentaFija())
                .publicado(d.isPublicado())
                .imagenPortadaUrl(d.getImagenPortadaUrl())
                .showroomVirtualUrl(d.getShowroomVirtualUrl())
                .brochurePlanosUrl(d.getBrochurePlanosUrl())
                .avanceObraUrl(d.getAvanceObraUrl())
                .solicitarInformacionUrl(d.getSolicitarInformacionUrl())
                .build();
    }
}
