package ar.com.solaresdedonato.api.adapter.in.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class ManagementSecurityConfig {

    /**
     * Solo matchea cuando el actuator corre en un puerto REALMENTE distinto (Spring Boot
     * levanta un segundo connector embebido aparte). En Railway/Render/Fly, que exponen
     * un único puerto, management.server.port se configura igual a server.port — ahí
     * matchear por número de puerto matchearía TODO (todo request cae en ese mismo
     * puerto), permitiendo sin JWT cualquier endpoint de la API. El {@code managementPort
     * != serverPort} evita justamente eso: con puertos iguales esta chain no matchea
     * nada, y /actuator/health + /actuator/info quedan permitAll en SecurityConfig
     * en su lugar (el resto de /actuator/** sigue exigiendo JWT).
     */
    @Bean
    @Order(1)
    public SecurityFilterChain managementFilterChain(
            HttpSecurity http,
            @Value("${server.port}") int serverPort,
            @Value("${management.server.port}") int managementPort) throws Exception {
        http
                .securityMatcher(request -> managementPort != serverPort && request.getServerPort() == managementPort)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS));
        return http.build();
    }
}