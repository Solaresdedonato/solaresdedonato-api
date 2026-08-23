package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.TokenDto;
import ar.com.solaresdedonato.api.adapter.in.model.request.LoginRequest;
import ar.com.solaresdedonato.api.core.usecase.auth.Login;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Login login;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(TokenDto.fromDomain(login.execute(request.toCommand())));
    }
}
