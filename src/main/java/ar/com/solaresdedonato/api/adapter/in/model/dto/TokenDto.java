package ar.com.solaresdedonato.api.adapter.in.model.dto;

import ar.com.solaresdedonato.api.core.domain.SesionAdmin;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String token;

    public static TokenDto fromDomain(SesionAdmin sesion) {
        return TokenDto.builder()
                .token(sesion.getToken())
                .build();
    }
}
