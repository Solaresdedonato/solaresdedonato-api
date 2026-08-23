package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaContactoDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String proyectoInteres;
    private String mensaje;
    private String stampDate;

    public static ConsultaContactoDto fromDomain(ConsultaContacto d) {
        return ConsultaContactoDto.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .apellido(d.getApellido())
                .email(d.getEmail())
                .telefono(d.getTelefono())
                .proyectoInteres(d.getProyectoInteres())
                .mensaje(d.getMensaje())
                .stampDate(d.getStampDate().toString())
                .build();
    }
}
