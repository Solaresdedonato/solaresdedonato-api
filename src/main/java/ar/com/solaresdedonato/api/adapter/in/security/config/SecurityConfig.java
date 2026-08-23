package ar.com.solaresdedonato.api.adapter.in.security.config;

import ar.com.solaresdedonato.api.adapter.in.exception.AuthExceptionHandler;
import ar.com.solaresdedonato.api.adapter.in.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthExceptionHandler authenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/auth/**",
                                "/media/**",
                                "/health/**",
                                // Cuando management.server.port == server.port (deploy de un solo puerto,
                                // ver ManagementSecurityConfig) el actuator cae en esta chain: solo
                                // health/info quedan públicos, el resto de /actuator/** sigue con JWT.
                                "/actuator/health",
                                "/actuator/health/**",
                                "/actuator/info",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/configuration/**",
                                "/error"
                        ).permitAll()
                        // Altas públicas sin JWT (formularios de la web)
                        .requestMatchers(HttpMethod.POST, "/v1/consulta-contacto").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/registro-broker").permitAll()
                        // Carrusel de inicio del sitio público (ver ContenidoController.listarHero)
                        .requestMatchers(HttpMethod.GET, "/v1/contenido/hero").permitAll()
                        // /v1/desarrollo/admin/** va antes: es más específico que el wildcard público de abajo
                        .requestMatchers(HttpMethod.GET, "/v1/desarrollo/admin/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/desarrollo", "/v1/desarrollo/*").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}