package ar.com.solaresdedonato.api.adapter.in.model.request;

import ar.com.solaresdedonato.api.core.command.LoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
