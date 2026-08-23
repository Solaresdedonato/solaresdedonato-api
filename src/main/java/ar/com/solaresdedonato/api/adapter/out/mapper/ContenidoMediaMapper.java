package ar.com.solaresdedonato.api.adapter.out.mapper;

import ar.com.solaresdedonato.api.adapter.out.model.ContenidoMediaEntity;
import ar.com.solaresdedonato.api.adapter.out.model.DesarrolloEntity;
import ar.com.solaresdedonato.api.core.domain.ContenidoMedia;
import org.springframework.stereotype.Component;

@Component
public class ContenidoMediaMapper {

    public ContenidoMedia toDomain(ContenidoMediaEntity e) {
        return ContenidoMedia.builder()
                .id(e.getId())
                .desarrolloId(e.getDesarrollo() != null ? e.getDesarrollo().getId() : null)
                .desarrolloNombre(e.getDesarrollo() != null ? e.getDesarrollo().getNombre() : null)
                .tipo(e.getTipo())
                .titulo(e.getTitulo())
                .categoria(e.getCategoria())
                .descripcion(e.getDescripcion())
                .archivoUrl(e.getArchivoUrl())
                .videoUrl(e.getVideoUrl())
                .origenDriveFileId(e.getOrigenDriveFileId())
                .esPortada(e.isEsPortada())
                .orden(e.getOrden())
                .enable(e.isEnable())
                .stampApp(e.getStampApp())
                .stampUser(e.getStampUser())
                .stampDate(e.getStampDate())
                .build();
    }

    public ContenidoMediaEntity toEntity(ContenidoMedia d) {
        DesarrolloEntity desarrolloRef = d.getDesarrolloId() != null
                ? DesarrolloEntity.builder().id(d.getDesarrolloId()).build()
                : null;

        return ContenidoMediaEntity.builder()
                .id(d.getId())
                .desarrollo(desarrolloRef)
                .tipo(d.getTipo())
                .titulo(d.getTitulo())
                .categoria(d.getCategoria())
                .descripcion(d.getDescripcion())
                .archivoUrl(d.getArchivoUrl())
                .videoUrl(d.getVideoUrl())
                .origenDriveFileId(d.getOrigenDriveFileId())
                .esPortada(d.isEsPortada())
                .orden(d.getOrden())
                .enable(d.isEnable())
                .stampApp(d.getStampApp())
                .stampUser(d.getStampUser())
                .stampDate(d.getStampDate())
                .build();
    }
}
