package ar.com.solaresdedonato.api.core.exception;

import java.util.Map;

public class BadRequestException extends AException {
    public BadRequestException(String message, String code, Map<String, Object> details) {
        super(message, code);
        this.getDetails().putAll(details);
    }
}