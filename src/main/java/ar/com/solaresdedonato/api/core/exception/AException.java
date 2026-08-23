package ar.com.solaresdedonato.api.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public abstract class AException extends RuntimeException {

    private final String code;
    private final Map<String, Object> details;

    protected AException(String message, String code) {
        super(message);
        this.code = code;
        this.details = new HashMap<>();
    }
}
