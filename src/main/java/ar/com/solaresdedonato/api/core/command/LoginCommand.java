package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginCommand {
    private final String email;
    private final String password;
}
