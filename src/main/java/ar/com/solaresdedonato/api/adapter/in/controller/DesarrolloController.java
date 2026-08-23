package ar.com.solaresdedonato.api.adapter.in.controller;

import ar.com.solaresdedonato.api.adapter.in.model.dto.ContenidoMediaDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.DesarrolloDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.PageDto;
import ar.com.solaresdedonato.api.adapter.in.model.dto.ResumenDesarrollosDto;
import ar.com.solaresdedonato.api.adapter.in.model.request.DesarrolloRequest;
import ar.com.solaresdedonato.api.adapter.in.utils.StampUtils;
import ar.com.solaresdedonato.api.core.domain.DesarrolloDetalle;
import ar.com.solaresdedonato.api.core.ports.PageQuery;
import ar.com.solaresdedonato.api.core.usecase.desarrollo.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/desarrollo")
@RequiredArgsConstructor
public class DesarrolloController {

    private final ListarDesarrollosPublicados listarDesarrollosPublicados;
    private final ObtenerDesarrolloPorSlug obtenerDesarrolloPorSlug;
    private final ListarDesarrollosAdmin listarDesarrollosAdmin;
    private final ObtenerDesarrolloPorId obtenerDesarrolloPorId;
    private final ObtenerResumenDesarrollos obtenerResumenDesarrollos;
    private final CrearDesarrollo crearDesarrollo;
    private final ActualizarDesarrollo actualizarDesarrollo;
    private final EliminarDesarrollo eliminarDesarrollo;

    @GetMapping
    public ResponseEntity<PageDto<DesarrolloDto>> listarPublicados(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String zona,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var resultado = listarDesarrollosPublicados.execute(estado, zona, PageQuery.of(page, size));
        return ResponseEntity.ok(PageDto.fromPageResult(resultado, DesarrolloDto::fromDomain));
    }

    @GetMapping("/admin")
    public ResponseEntity<PageDto<DesarrolloDto>> listarAdmin(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) Boolean publicado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var resultado = listarDesarrollosAdmin.execute(estado, zona, publicado, PageQuery.of(page, size));
        return ResponseEntity.ok(PageDto.fromPageResult(resultado, DesarrolloDto::fromDomain));
    }

    @GetMapping("/admin/resumen")
    public ResponseEntity<ResumenDesarrollosDto> resumen() {
        return ResponseEntity.ok(ResumenDesarrollosDto.fromDomain(obtenerResumenDesarrollos.execute()));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<DesarrolloDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(DesarrolloDto.fromDomain(obtenerDesarrolloPorId.execute(id)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<DesarrolloDto> obtenerPorSlug(@PathVariable String slug) {
        DesarrolloDetalle detalle = obtenerDesarrolloPorSlug.execute(slug);
        DesarrolloDto dto = DesarrolloDto.fromDomain(detalle.getDesarrollo());
        dto.setGaleria(detalle.getGaleria().stream().map(ContenidoMediaDto::fromDomain).toList());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DesarrolloDto> crear(@Valid @RequestBody DesarrolloRequest request) {
        var creado = crearDesarrollo.execute(request.toCommand(), StampUtils.build());
        return ResponseEntity.status(HttpStatus.CREATED).body(DesarrolloDto.fromDomain(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesarrolloDto> actualizar(@PathVariable Long id, @Valid @RequestBody DesarrolloRequest request) {
        var actualizado = actualizarDesarrollo.execute(id, request.toCommand(), StampUtils.build());
        return ResponseEntity.ok(DesarrolloDto.fromDomain(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarDesarrollo.execute(id);
        return ResponseEntity.noContent().build();
    }
}
