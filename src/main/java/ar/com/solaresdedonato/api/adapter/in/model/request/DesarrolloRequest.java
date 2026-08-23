package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.DesarrolloCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesarrolloRequest {

    @NotBlank(message = "El slug es requerido")
    private String slug;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 200)
    private String nombre;

    @NotBlank(message = "La zona es requerida")
    @Size(max = 150)
    private String zona;

    @NotBlank(message = "La dirección es requerida")
    @Size(max = 300)
    private String direccion;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotNull
    @Size(min = 4, max = 4, message = "Debe haber exactamente 4 features")
    @Valid
    private List<DesarrolloFeatureRequest> features;

    @NotNull
    @Valid
    private CercaniasRequest cercanias;

    private boolean instrumentoTokenizacion;

    private boolean instrumentoRentaFija;

    private boolean publicar;

    /** Enlaces opcionales de la ficha pública — ninguno es obligatorio. */
    @URL(message = "La URL del showroom virtual no es válida")
    @Size(max = 500)
    private String showroomVirtualUrl;

    @URL(message = "La URL del brochure/planos no es válida")
    @Size(max = 500)
    private String brochurePlanosUrl;

    @URL(message = "La URL de avance de obra no es válida")
    @Size(max = 500)
    private String avanceObraUrl;

    @URL(message = "La URL de solicitar información no es válida")
    @Size(max = 500)
    private String solicitarInformacionUrl;

    public DesarrolloCommand toCommand() {
        return DesarrolloCommand.builder()
                .slug(this.slug)
                .nombre(this.nombre)
                .zona(this.zona)
                .direccion(this.direccion)
                .estado(this.estado)
                .descripcion(this.descripcion)
                .features(this.features.stream().map(DesarrolloFeatureRequest::toDomain).toList())
                .cercanias(this.cercanias.toDomain())
                .instrumentoTokenizacion(this.instrumentoTokenizacion)
                .instrumentoRentaFija(this.instrumentoRentaFija)
                .publicar(this.publicar)
                .showroomVirtualUrl(this.showroomVirtualUrl)
                .brochurePlanosUrl(this.brochurePlanosUrl)
                .avanceObraUrl(this.avanceObraUrl)
                .solicitarInformacionUrl(this.solicitarInformacionUrl)
                .build();
    }
}
