package ar.com.solaresdedonato.api.core.usecase.consultacontacto;

import ar.com.solaresdedonato.api.core.command.ConsultaContactoCommand;
import ar.com.solaresdedonato.api.core.command.StampCommand;
import ar.com.solaresdedonato.api.core.domain.ConsultaContacto;
import ar.com.solaresdedonato.api.core.ports.repository.ConsultaContactoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearConsultaContacto {

    private final ConsultaContactoRepositoryPort consultaContactoRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public ConsultaContacto execute(ConsultaContactoCommand command, StampCommand stamp) {
        ConsultaContacto domain = ConsultaContacto.builder()
                .nombre(command.getNombre())
                .apellido(command.getApellido())
                .email(command.getEmail())
                .telefono(command.getTelefono())
                .proyectoInteres(command.getProyectoInteres())
                .mensaje(command.getMensaje())
                .enable(true)
                .stampApp(stamp.getStampApp())
                .stampUser(stamp.getStampUser())
                .stampDate(stamp.getStampDate())
                .build();

        return consultaContactoRepositoryPort.save(domain);
    }
}
