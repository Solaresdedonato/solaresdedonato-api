package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.PageDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.RegistroBrokerDto;
import ar.com.solaresdedonato.api.adapter.in.model.request.RegistroBrokerRequest;
import ar.com.solaresdedonato.api.adapter.in.utils.StampUtils;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.usecase.registrobroker.CrearRegistroBroker;
import ar.com.solaresdedonato.api.core.usecase.registrobroker.EliminarRegistroBroker;
import ar.com.solaresdedonato.api.core.usecase.registrobroker.ListarRegistroBroker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/registro-broker")
@RequiredArgsConstructor
public class RegistroBrokerController {

    private final CrearRegistroBroker crearRegistroBroker;
    private final ListarRegistroBroker listarRegistroBroker;
    private final EliminarRegistroBroker eliminarRegistroBroker;

    @PostMapping
    public ResponseEntity<RegistroBrokerDto> crear(@Valid @RequestBody RegistroBrokerRequest request) {
        var creado = crearRegistroBroker.execute(request.toCommand(), StampUtils.buildPublic());
        return ResponseEntity.status(HttpStatus.CREATED).body(RegistroBrokerDto.fromDomain(creado));
    }

    @GetMapping
    public ResponseEntity<PageDto<RegistroBrokerDto>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var resultado = listarRegistroBroker.execute(PageQuery.of(page, size));
        return ResponseEntity.ok(PageDto.fromPageResult(resultado, RegistroBrokerDto::fromDomain));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarRegistroBroker.execute(id);
        return ResponseEntity.noContent().build();
    }
}
