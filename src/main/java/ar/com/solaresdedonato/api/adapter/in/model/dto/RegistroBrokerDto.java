package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegistroBrokerDto {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String inmobiliaria;
    private String matricula;
    private String experiencia;
    private String zonaOperacion;
    private List<String> tipoOperaciones;
    private String operacionesCerradas;
    private String mensaje;
    private String stampDate;

    public static RegistroBrokerDto fromDomain(RegistroBroker d) {
        return RegistroBrokerDto.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .email(d.getEmail())
                .telefono(d.getTelefono())
                .inmobiliaria(d.getInmobiliaria())
                .matricula(d.getMatricula())
                .experiencia(d.getExperiencia())
                .zonaOperacion(d.getZonaOperacion())
                .tipoOperaciones(d.getTipoOperaciones())
                .operacionesCerradas(d.getOperacionesCerradas())
                .mensaje(d.getMensaje())
                .stampDate(d.getStampDate().toString())
                .build();
    }
}
