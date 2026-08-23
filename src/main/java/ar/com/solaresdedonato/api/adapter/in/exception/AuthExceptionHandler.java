package ar.com.solaresdedonato.api.adapter.in.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import ar.com.solaresdedonato.api.adapter.in.exception.model.ErrorResponse;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthExceptionHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.warn("Autenticación fallida: {}", authException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_AUTENTICACION)
                .message("Acceso denegado: se requiere autenticación")
                .detail(Map.of("error", authException.getMessage()))
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}