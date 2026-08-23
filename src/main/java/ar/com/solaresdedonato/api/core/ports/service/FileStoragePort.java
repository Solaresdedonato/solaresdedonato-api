package ar.com.solaresdedonato.api.core.ports.service;

public interface FileStoragePort {
    /** Guarda el archivo y devuelve la URL pública para servirlo. */
    String store(byte[] bytes, String filename, String subfolder);

    void delete(String url);
}
