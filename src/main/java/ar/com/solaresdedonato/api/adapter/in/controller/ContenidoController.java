package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.ContenidoMediaDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.PageDto;
import ar.com.solaresdedonato.api.adapter.in.model.request.CrearContenidoRequest;
import ar.com.solaresdedonato.api.adapter.in.model.request.ImportarContenidoDesdeDriveRequest;
import ar.com.solaresdedonato.api.adapter.in.utils.StampUtils;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.usecase.contenido.CrearContenido;
import ar.com.solaresdedonato.api.core.usecase.contenido.EliminarContenido;
import ar.com.solaresdedonato.api.core.usecase.contenido.ImportarContenidoDesdeDrive;
import ar.com.solaresdedonato.api.core.usecase.contenido.ListarContenido;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/contenido")
@RequiredArgsConstructor
public class ContenidoController {

    private final ListarContenido listarContenido;
    private final CrearContenido crearContenido;
    private final EliminarContenido eliminarContenido;
    private final ImportarContenidoDesdeDrive importarContenidoDesdeDrive;

    @GetMapping
    public ResponseEntity<PageDto<ContenidoMediaDto>> listar(
            @RequestParam(required = false) Long desarrolloId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var resultado = listarContenido.execute(desarrolloId, tipo, categoria, PageQuery.of(page, size));
        return ResponseEntity.ok(PageDto.fromPageResult(resultado, ContenidoMediaDto::fromDomain));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ContenidoMediaDto> crear(@Valid @ModelAttribute CrearContenidoRequest request) {
        var creado = crearContenido.execute(request.toCommand(), StampUtils.build());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContenidoMediaDto.fromDomain(creado));
    }

    @PostMapping("/importacion-drive")
    public ResponseEntity<ContenidoMediaDto> importarDesdeDrive(@Valid @RequestBody ImportarContenidoDesdeDriveRequest request) {
        var creado = importarContenidoDesdeDrive.execute(request.toCommand(), StampUtils.build());
        return ResponseEntity.status(HttpStatus.CREATED).body(ContenidoMediaDto.fromDomain(creado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarContenido.execute(id);
        return ResponseEntity.noContent().build();
    }
}
