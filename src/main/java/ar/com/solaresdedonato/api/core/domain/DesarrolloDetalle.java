package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** Read-model compuesto para el detalle público de un desarrollo (desarrollo + galería de contenido_media). */
@Getter
@Builder
public class DesarrolloDetalle {
    private final Desarrollo desarrollo;
    private final List<ContenidoMedia> galeria;
}
