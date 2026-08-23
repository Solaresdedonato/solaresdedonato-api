package ar.com.solaresdedonato.api.core.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImportarContenidoDriveCommand {
    private final String driveFileId;
    private final Long desarrolloId;
    private final String titulo;
    private final String categoria;
    private final String descripcion;
    private final boolean esPortada;
}
