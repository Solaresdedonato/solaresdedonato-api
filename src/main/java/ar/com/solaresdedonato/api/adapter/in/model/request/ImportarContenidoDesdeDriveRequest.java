package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.ImportarContenidoDriveCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImportarContenidoDesdeDriveRequest {

    @NotBlank(message = "El fileId de Drive es requerido")
    @Size(max = 120, message = "El fileId de Drive no puede superar los 120 caracteres")
    private String driveFileId;

    @NotBlank(message = "El título es requerido")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @NotNull(message = "El desarrollo es requerido")
    private Long desarrolloId;

    @NotBlank(message = "La categoría es requerida")
    @Pattern(
            regexp = "fachada|interior|amenities|obra|drone|institucional",
            message = "Categoría inválida")
    private String categoria;

    @Size(max = 2000, message = "La descripción no puede superar los 2000 caracteres")
    private String descripcion;

    private boolean esPortada;

    public ImportarContenidoDriveCommand toCommand() {
        return ImportarContenidoDriveCommand.builder()
                .driveFileId(this.driveFileId)
                .desarrolloId(this.desarrolloId)
                .titulo(this.titulo)
                .categoria(this.categoria)
                .descripcion(this.descripcion)
                .esPortada(this.esPortada)
                .build();
    }
}
