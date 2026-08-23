package ar.com.solaresdedonato.api.adapter.in.security.filter;

import ar.com.solaresdedonato.api.adapter.in.security.model.JwtAuthenticationDetails;
import ar.com.solaresdedonato.api.adapter.in.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            if (jwtUtils.isTokenValid(jwt)) {
                String username = jwtUtils.extractUsername(jwt);
                String userId = jwtUtils.extractUserId(jwt);
                String systemId = jwtUtils.extractSystemId(jwt);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );

                authToken.setDetails(new JwtAuthenticationDetails(request, userId, systemId));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Autenticación establecida - user: {}, systemId: {}", username, systemId);
            } else {
                log.warn("Token inválido o expirado");
            }
        } catch (Exception e) {
            log.error("Error procesando JWT: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServerPort() == 9090;
    }
}