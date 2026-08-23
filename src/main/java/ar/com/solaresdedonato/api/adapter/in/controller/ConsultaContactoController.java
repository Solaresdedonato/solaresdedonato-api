package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.ConsultaContactoDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.PageDto;
import ar.com.solaresdedonato.api.adapter.in.model.request.ConsultaContactoRequest;
import ar.com.solaresdedonato.api.adapter.in.utils.StampUtils;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.usecase.consultacontacto.CrearConsultaContacto;
import ar.com.solaresdedonato.api.core.usecase.consultacontacto.EliminarConsultaContacto;
import ar.com.solaresdedonato.api.core.usecase.consultacontacto.ListarConsultaContacto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/consulta-contacto")
@RequiredArgsConstructor
public class ConsultaContactoController {

    private final CrearConsultaContacto crearConsultaContacto;
    private final ListarConsultaContacto listarConsultaContacto;
    private final EliminarConsultaContacto eliminarConsultaContacto;

    @PostMapping
    public ResponseEntity<ConsultaContactoDto> crear(@Valid @RequestBody ConsultaContactoRequest request) {
        var creado = crearConsultaContacto.execute(request.toCommand(), StampUtils.buildPublic());
        return ResponseEntity.status(HttpStatus.CREATED).body(ConsultaContactoDto.fromDomain(creado));
    }

    @GetMapping
    public ResponseEntity<PageDto<ConsultaContactoDto>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var resultado = listarConsultaContacto.execute(PageQuery.of(page, size));
        return ResponseEntity.ok(PageDto.fromPageResult(resultado, ConsultaContactoDto::fromDomain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarConsultaContacto.execute(id);
        return ResponseEntity.noContent().build();
    }
}
