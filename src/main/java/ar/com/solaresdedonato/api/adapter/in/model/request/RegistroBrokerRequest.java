package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.RegistroBrokerCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroBrokerRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 160)
    private String nombre;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 40)
    private String telefono;

    @NotBlank(message = "La inmobiliaria es requerida")
    @Size(max = 200)
    private String inmobiliaria;

    @Size(max = 80)
    private String matricula;

    /** Select opcional en el form público — puede llegar como cadena vacía, se normaliza a null. */
    private String experiencia;

    @NotBlank(message = "La zona de operación es requerida")
    @Size(max = 80)
    private String zonaOperacion;

    @NotNull
    private List<String> tipoOperaciones;

    private String operacionesCerradas;

    private String mensaje;

    public RegistroBrokerCommand toCommand() {
        return RegistroBrokerCommand.builder()
                .nombre(this.nombre)
                .email(this.email)
                .telefono(this.telefono)
                .inmobiliaria(this.inmobiliaria)
                .matricula(blankToNull(this.matricula))
                .experiencia(blankToNull(this.experiencia))
                .zonaOperacion(this.zonaOperacion)
                .tipoOperaciones(this.tipoOperaciones)
                .operacionesCerradas(blankToNull(this.operacionesCerradas))
                .mensaje(blankToNull(this.mensaje))
                .build();
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
