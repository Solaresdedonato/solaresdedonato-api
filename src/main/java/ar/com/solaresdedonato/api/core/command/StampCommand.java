package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StampCommand {
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
