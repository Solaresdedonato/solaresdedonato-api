package ar.com.solaresdedonato.api.core.usecase.registrobroker;

import ar.com.solaresdedonato.api.core.command.RegistroBrokerCommand;
import ar.com.solaresdedonato.api.core.command.StampCommand;
import ar.com.solaresdedonato.api.core.domain.RegistroBroker;
import ar.com.solaresdedonato.api.core.ports.repository.RegistroBrokerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearRegistroBroker {

    private final RegistroBrokerRepositoryPort registroBrokerRepositoryPort;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public RegistroBroker execute(RegistroBrokerCommand command, StampCommand stamp) {
        RegistroBroker domain = RegistroBroker.builder()
                .nombre(command.getNombre())
                .email(command.getEmail())
                .telefono(command.getTelefono())
                .inmobiliaria(command.getInmobiliaria())
                .matricula(command.getMatricula())
                .experiencia(command.getExperiencia())
                .zonaOperacion(command.getZonaOperacion())
                .tipoOperaciones(command.getTipoOperaciones())
                .operacionesCerradas(command.getOperacionesCerradas())
                .mensaje(command.getMensaje())
                .enable(true)
                .stampApp(stamp.getStampApp())
                .stampUser(stamp.getStampUser())
                .stampDate(stamp.getStampDate())
                .build();

        return registroBrokerRepositoryPort.save(domain);
    }
}
