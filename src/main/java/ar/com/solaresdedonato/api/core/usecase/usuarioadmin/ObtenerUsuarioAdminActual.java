package ar.com.solaresdedonato.api.core.usecase.usuarioadmin;

import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;
import ar.com.solaresdedonato.api.core.exception.NotFoundException;
import ar.com.solaresdedonato.api.core.ports.repository.UsuarioAdminRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ObtenerUsuarioAdminActual {

    private final UsuarioAdminRepositoryPort usuarioAdminRepositoryPort;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public UsuarioAdmin execute(Long id) {
        return usuarioAdminRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Usuario administrador no encontrado",
                        ErrorCodes.USUARIO_ADMIN_NO_ENCONTRADO,
                        Map.of("id", id)
                ));
    }
}
