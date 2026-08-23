package ar.com.solaresdedonato.api.core.usecase.auth;

import ar.com.solaresdedonato.api.adapter.in.security.utils.JwtUtils;
import ar.com.solaresdedonato.api.core.command.LoginCommand;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.domain.SesionAdmin;
import ar.com.solaresdedonato.api.core.domain.UsuarioAdmin;
import ar.com.solaresdedonato.api.core.exception.UnauthorizedException;
import ar.com.solaresdedonato.api.core.ports.repository.UsuarioAdminRepositoryPort;
import ar.com.solaresdedonato.api.core.ports.service.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Llama a JwtUtils directamente en vez de a través de un puerto: excepción pragmática
 * documentada en el plan (ya es una utilidad de infraestructura transversal del scaffold).
 */
@Service
@RequiredArgsConstructor
public class Login {

    private final UsuarioAdminRepositoryPort usuarioAdminRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final JwtUtils jwtUtils;

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public SesionAdmin execute(LoginCommand command) {
        UsuarioAdmin usuario = usuarioAdminRepositoryPort.findByEmail(command.getEmail())
                .filter(u -> passwordEncoderPort.matches(command.getPassword(), u.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException(
                        "Email o contraseña inválidos",
                        ErrorCodes.CREDENCIALES_INVALIDAS,
                        Map.of()
                ));

        UsuarioAdmin actualizado = usuarioAdminRepositoryPort.save(
                usuario.toBuilder().ultimoLoginDate(LocalDateTime.now()).build()
        );

        String token = jwtUtils.generateToken(actualizado.getEmail(), String.valueOf(actualizado.getId()));

        return SesionAdmin.builder()
                .token(token)
                .usuario(actualizado)
                .build();
    }
}
