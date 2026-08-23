package ar.com.solaresdedonato.api.core.exception;

import java.util.Map;

public class UnauthorizedException extends AException {
    public UnauthorizedException(String message, String code, Map<String, Object> details) {
        super(message, code);
        this.getDetails().putAll(details);
    }
}
