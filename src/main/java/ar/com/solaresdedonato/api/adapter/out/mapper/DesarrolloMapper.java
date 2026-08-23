package ar.com.solaresdedonato.api.adapter.out.mapper;

import ar.com.solaresdedonato.api.adapter.out.model.DesarrolloEntity;
import ar.com.solaresdedonato.api.core.domain.Desarrollo;
import ar.com.solaresdedonato.api.core.domain.DesarrolloCercanias;
import ar.com.solaresdedonato.api.core.domain.DesarrolloFeature;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DesarrolloMapper {

    public Desarrollo toDomain(DesarrolloEntity e) {
        return Desarrollo.builder()
                .id(e.getId())
                .slug(e.getSlug())
                .nombre(e.getNombre())
                .zona(e.getZona())
                .direccion(e.getDireccion())
                .estado(e.getEstado())
                .descripcion(e.getDescripcion())
                .features(featuresToDomain(e.getFeatures()))
                .cercanias(cercaniasToDomain(e.getCercanias()))
                .instrumentoTokenizacion(e.isInstrumentoTokenizacion())
                .instrumentoRentaFija(e.isInstrumentoRentaFija())
                .publicado(e.isPublicado())
                .imagenPortadaUrl(e.getImagenPortadaUrl())
                .enable(e.isEnable())
                .stampApp(e.getStampApp())
                .stampUser(e.getStampUser())
                .stampDate(e.getStampDate())
                .build();
    }

    public DesarrolloEntity toEntity(Desarrollo d) {
        return DesarrolloEntity.builder()
                .id(d.getId())
                .slug(d.getSlug())
                .nombre(d.getNombre())
                .zona(d.getZona())
                .direccion(d.getDireccion())
                .estado(d.getEstado())
                .descripcion(d.getDescripcion())
                .features(featuresToEntity(d.getFeatures()))
                .cercanias(cercaniasToEntity(d.getCercanias()))
                .instrumentoTokenizacion(d.isInstrumentoTokenizacion())
                .instrumentoRentaFija(d.isInstrumentoRentaFija())
                .publicado(d.isPublicado())
                .imagenPortadaUrl(d.getImagenPortadaUrl())
                .enable(d.isEnable())
                .stampApp(d.getStampApp())
                .stampUser(d.getStampUser())
                .stampDate(d.getStampDate())
                .build();
    }

    private List<DesarrolloFeature> featuresToDomain(List<Map<String, String>> raw) {
        if (raw == null) return Collections.emptyList();
        return raw.stream()
                .map(f -> DesarrolloFeature.builder()
                        .clave(f.get("clave"))
                        .titulo(f.get("titulo"))
                        .texto(f.get("texto"))
                        .build())
                .toList();
    }

    private List<Map<String, String>> featuresToEntity(List<DesarrolloFeature> features) {
        if (features == null) return Collections.emptyList();
        return features.stream()
                .map(f -> Map.of("clave", f.getClave(), "titulo", f.getTitulo(), "texto", f.getTexto()))
                .toList();
    }

    private DesarrolloCercanias cercaniasToDomain(Map<String, List<String>> raw) {
        if (raw == null) return DesarrolloCercanias.builder().educacion(List.of()).transporte(List.of()).comercios(List.of()).salud(List.of()).build();
        return DesarrolloCercanias.builder()
                .educacion(raw.getOrDefault("educacion", List.of()))
                .transporte(raw.getOrDefault("transporte", List.of()))
                .comercios(raw.getOrDefault("comercios", List.of()))
                .salud(raw.getOrDefault("salud", List.of()))
                .build();
    }

    private Map<String, List<String>> cercaniasToEntity(DesarrolloCercanias cercanias) {
        if (cercanias == null) {
            return Map.of("educacion", List.of(), "transporte", List.of(), "comercios", List.of(), "salud", List.of());
        }
        return Map.of(
                "educacion", cercanias.getEducacion() != null ? cercanias.getEducacion() : List.of(),
                "transporte", cercanias.getTransporte() != null ? cercanias.getTransporte() : List.of(),
                "comercios", cercanias.getComercios() != null ? cercanias.getComercios() : List.of(),
                "salud", cercanias.getSalud() != null ? cercanias.getSalud() : List.of()
        );
    }
}
