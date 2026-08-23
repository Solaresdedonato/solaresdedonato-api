package ar.com.solaresdedonato.api.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class ContenidoMedia {
    private final Long id;
    private final Long desarrolloId;
    private final String desarrolloNombre;
    private final String tipo;
    private final String titulo;
    private final String categoria;
    private final String descripcion;
    private final String archivoUrl;
    private final String videoUrl;
    private final String origenDriveFileId;
    private final boolean esPortada;
    private final short orden;
    private final boolean enable;
    private final String stampApp;
    private final String stampUser;
    private final LocalDateTime stampDate;
}
