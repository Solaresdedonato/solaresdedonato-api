package ar.com.solaresdedonato.api.adapter.out.mapper;

import ar.com.solaresdedonato.api.adapter.out.model.RegistroBrokerEntity;
import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import org.springframework.stereotype.Component;

@Component
public class RegistroBrokerMapper {

    public RegistroBroker toDomain(RegistroBrokerEntity e) {
        return RegistroBroker.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .email(e.getEmail())
                .telefono(e.getTelefono())
                .inmobiliaria(e.getInmobiliaria())
                .matricula(e.getMatricula())
                .experiencia(e.getExperiencia())
                .zonaOperacion(e.getZonaOperacion())
                .tipoOperaciones(e.getTipoOperaciones())
                .operacionesCerradas(e.getOperacionesCerradas())
                .mensaje(e.getMensaje())
                .enable(e.isEnable())
                .stampApp(e.getStampApp())
                .stampUser(e.getStampUser())
                .stampDate(e.getStampDate())
                .build();
    }

    public RegistroBrokerEntity toEntity(RegistroBroker d) {
        return RegistroBrokerEntity.builder()
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
                .enable(d.isEnable())
                .stampApp(d.getStampApp())
                .stampUser(d.getStampUser())
                .stampDate(d.getStampDate())
                .build();
    }
}
