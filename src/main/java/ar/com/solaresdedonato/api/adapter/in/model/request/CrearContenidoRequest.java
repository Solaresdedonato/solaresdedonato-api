package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.ContenidoMediaCommand;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.exception.BadRequestException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Data
public class CrearContenidoRequest {

    @NotBlank(message = "El tipo es requerido")
    @Pattern(regexp = "foto|video", message = "El tipo debe ser 'foto' o 'video'")
    private String tipo;

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

    @Size(max = 500, message = "La URL del video no puede superar los 500 caracteres")
    private String videoUrl;

    private boolean esPortada;

    private MultipartFile archivo;

    public ContenidoMediaCommand toCommand() {
        byte[] bytes = null;
        String nombreOriginal = null;
        String contentType = null;

        if (archivo != null && !archivo.isEmpty()) {
            try {
                bytes = archivo.getBytes();
            } catch (IOException e) {
                throw new BadRequestException("No se pudo leer el archivo enviado", ErrorCodes.ERROR_VALIDACION, Map.of());
            }
            nombreOriginal = archivo.getOriginalFilename();
            contentType = archivo.getContentType();
        }

        return ContenidoMediaCommand.builder()
                .desarrolloId(this.desarrolloId)
                .tipo(this.tipo)
                .titulo(this.titulo)
                .categoria(this.categoria)
                .descripcion(this.descripcion)
                .videoUrl(this.videoUrl)
                .esPortada(this.esPortada)
                .archivoBytes(bytes)
                .archivoNombreOriginal(nombreOriginal)
                .archivoContentType(contentType)
                .build();
    }
}
