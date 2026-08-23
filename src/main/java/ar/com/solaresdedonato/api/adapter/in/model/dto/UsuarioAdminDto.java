package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioAdminDto {
    private String nombre;
    private String email;
    private String rol;

    public static UsuarioAdminDto fromDomain(UsuarioAdmin d) {
        return UsuarioAdminDto.builder()
                .nombre(d.getNombre())
                .email(d.getEmail())
                .rol(d.getRol())
                .build();
    }
}
