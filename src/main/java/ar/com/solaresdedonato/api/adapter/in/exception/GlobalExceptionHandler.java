package ar.com.solaresdedonato.api.adapter.in.exception;

import ar.com.solaresdedonato.api.adapter.in.exception.model.ErrorResponse;
import ar.com.solaresdedonato.api.core.constants.ErrorCodes;
import ar.com.solaresdedonato.api.core.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        log.error("Error inesperado: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_INTERNO)
                .message("Error interno del servidor")
                .detail(Map.of("error", "Ocurrió un error inesperado"))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            WebRequest request) {

        log.warn("Acceso denegado: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ACCESO_DENEGADO)
                .message("Acceso denegado: permisos insuficientes")
                .detail(Map.of("error", ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.warn("Validación fallida: {} errores", ex.getErrorCount());

        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_VALIDACION)
                .message("Errores de validación en la solicitud")
                .detail(details)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NotFoundException ex,
            WebRequest request) {

        log.warn("Entidad no encontrada: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ex.getCode())
                .message(ex.getMessage())
                .detail(buildDetail(ex))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex,
            WebRequest request) {

        log.warn("Violación de regla de negocio: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ex.getCode())
                .message(ex.getMessage())
                .detail(buildDetail(ex))
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ConflictException ex,
            WebRequest request) {

        log.warn("Conflicto: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ex.getCode())
                .message(ex.getMessage())
                .detail(buildDetail(ex))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex,
            WebRequest request) {

        log.warn("Solicitud inválida: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ex.getCode())
                .message(ex.getMessage())
                .detail(buildDetail(ex))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex,
            WebRequest request) {

        log.warn("No autorizado: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ex.getCode())
                .message(ex.getMessage())
                .detail(buildDetail(ex))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        // Última línea de defensa: un CHECK/UNIQUE de la base que ninguna validación de
        // arriba atajó (ej. una categoría con typo que igual pasó el @Pattern porque se
        // agregó después). Sin este handler caía en el catch-all de Exception -> 500.
        log.warn("Violación de integridad de datos: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_VALIDACION)
                .message("La operación viola una restricción de datos")
                .detail(Map.of("error", "Verificá los valores enviados"))
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            HandlerMethodValidationException ex,
            WebRequest request) {

        log.error("Excepción de validación", ex);

        Map<String, Object> details = new HashMap<>();

        ex.getValueResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();

            result.getResolvableErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    String fieldName = fieldError.getField();
                    String message = fieldError.getDefaultMessage();
                    details.put(parameterName + "." + fieldName, message);
                    log.error("Error de validación: campo={}, mensaje={}", fieldName, message);
                } else {
                    String errorCode = Arrays.toString(error.getCodes());
                    String message = error.getDefaultMessage();
                    details.put(parameterName + "." + errorCode, message);
                    log.error("Error de validación: código={}, mensaje={}", errorCode, message);
                }
            });
        });

        if (details.isEmpty()) {
            details.put("error", "Validación fallida sin detalles específicos");
            details.put("message", ex.getMessage());
        }

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_VALIDACION)
                .message("Errores de validación en la solicitud")
                .detail(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Endpoint no existe o falta parámetro obligatorio en el path")
                .codigoError(ErrorCodes.ENDPOINT_NO_ENCONTRADO)
                .detail(Map.of("path", ex.getRequestURL(), "method", ex.getHttpMethod()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex,
            WebRequest request) {

        log.warn("Parámetro requerido faltante: {}", ex.getParameterName());

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .codigoError(ErrorCodes.ERROR_VALIDACION)
                .message("Parámetro requerido faltante")
                .detail(Map.of("parameter", ex.getParameterName(), "type", ex.getParameterType()))
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    private Map<String, Object> buildDetail(AException ex) {
        return ex.getDetails() != null && !ex.getDetails().isEmpty()
                ? ex.getDetails()
                : new HashMap<>();
    }
}