package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContenidoMediaCommand {
    private final Long desarrolloId;
    private final String tipo;
    private final String titulo;
    private final String categoria;
    private final String descripcion;
    private final String videoUrl;
    private final boolean esPortada;
    private final byte[] archivoBytes;
    private final String archivoNombreOriginal;
    private final String archivoContentType;
}
