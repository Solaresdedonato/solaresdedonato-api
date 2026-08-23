package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.UsuarioAdminDto;
import ar.com.solaresdedonato.api.adapter.in.utils.SecurityUtils;
import ar.com.solaresdedonato.api.core.usecase.usuarioadmin.ObtenerUsuarioAdminActual;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usuario-admin")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final ObtenerUsuarioAdminActual obtenerUsuarioAdminActual;

    @GetMapping("/me")
    public ResponseEntity<UsuarioAdminDto> me() {
        Long id = Long.valueOf(SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(UsuarioAdminDto.fromDomain(obtenerUsuarioAdminActual.execute(id)));
    }
}
