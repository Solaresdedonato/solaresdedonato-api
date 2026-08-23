package ar.com.solaresdedonato.api.adapter.out.mapper;

import ar.com.solaresdedonato.api.adapter.out.model.UsuarioAdminEntity;
import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAdminMapper {

    public UsuarioAdmin toDomain(UsuarioAdminEntity e) {
        return UsuarioAdmin.builder()
                .id(e.getId())
                .email(e.getEmail())
                .passwordHash(e.getPasswordHash())
                .nombre(e.getNombre())
                .rol(e.getRol())
                .ultimoLoginDate(e.getUltimoLoginDate())
                .enable(e.isEnable())
                .stampApp(e.getStampApp())
                .stampUser(e.getStampUser())
                .stampDate(e.getStampDate())
                .build();
    }

    public UsuarioAdminEntity toEntity(UsuarioAdmin d) {
        return UsuarioAdminEntity.builder()
                .id(d.getId())
                .email(d.getEmail())
                .passwordHash(d.getPasswordHash())
                .nombre(d.getNombre())
                .rol(d.getRol())
                .ultimoLoginDate(d.getUltimoLoginDate())
                .enable(d.isEnable())
                .stampApp(d.getStampApp())
                .stampUser(d.getStampUser())
                .stampDate(d.getStampDate())
                .build();
    }
}
