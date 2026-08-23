package ar.com.solaresdedonato.api.adapter.in.utils;

import ar.com.solaresdedonato.api.core.command.StampCommand;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static ar.com.solaresdedonato.api.core.constants.Constants.STAMP_APP;

@NoArgsConstructor
public final class StampUtils {

    private static final String USUARIO_PUBLICO = "sistema-publico";

    public static StampCommand build() {
        return StampCommand.builder()
                .stampUser(SecurityUtils.getCurrentUserId())
                .stampApp(STAMP_APP)
                .stampDate(LocalDateTime.now())
                .build();
    }

    /** Para las altas públicas sin JWT (consulta-contacto, registro-broker): no hay SecurityContext del que leer un usuario. */
    public static StampCommand buildPublic() {
        return StampCommand.builder()
                .stampUser(USUARIO_PUBLICO)
                .stampApp(STAMP_APP)
                .stampDate(LocalDateTime.now())
                .build();
    }
}