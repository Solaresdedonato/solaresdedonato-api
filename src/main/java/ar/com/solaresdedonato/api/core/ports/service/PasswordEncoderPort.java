package ar.com.solaresdedonato.api.core.ports.service;

public interface PasswordEncoderPort {
    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
