package ar.com.solaresdedonato.api.adapter.out.mapper;

import ar.com.solaresdedonato.api.adapter.out.model.ConsultaContactoEntity;
import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import org.springframework.stereotype.Component;

@Component
public class ConsultaContactoMapper {

    public ConsultaContacto toDomain(ConsultaContactoEntity e) {
        return ConsultaContacto.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .apellido(e.getApellido())
                .email(e.getEmail())
                .telefono(e.getTelefono())
                .proyectoInteres(e.getProyectoInteres())
                .mensaje(e.getMensaje())
                .enable(e.isEnable())
                .stampApp(e.getStampApp())
                .stampUser(e.getStampUser())
                .stampDate(e.getStampDate())
                .build();
    }

    public ConsultaContactoEntity toEntity(ConsultaContacto d) {
        return ConsultaContactoEntity.builder()
                .id(d.getId())
                .nombre(d.getNombre())
                .apellido(d.getApellido())
                .email(d.getEmail())
                .telefono(d.getTelefono())
                .proyectoInteres(d.getProyectoInteres())
                .mensaje(d.getMensaje())
                .enable(d.isEnable())
                .stampApp(d.getStampApp())
                .stampUser(d.getStampUser())
                .stampDate(d.getStampDate())
                .build();
    }
}
