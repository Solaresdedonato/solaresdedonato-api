package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.ConsultaContactoCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaContactoRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 120)
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(max = 120)
    private String apellido;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 40)
    private String telefono;

    @Size(max = 200)
    private String proyectoInteres;

    @NotBlank(message = "El mensaje es requerido")
    private String mensaje;

    public ConsultaContactoCommand toCommand() {
        return ConsultaContactoCommand.builder()
                .nombre(this.nombre)
                .apellido(this.apellido)
                .email(this.email)
                .telefono(this.telefono)
                .proyectoInteres(this.proyectoInteres)
                .mensaje(this.mensaje)
                .build();
    }
}
