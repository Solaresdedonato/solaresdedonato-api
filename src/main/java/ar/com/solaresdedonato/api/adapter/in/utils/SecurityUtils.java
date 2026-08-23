package ar.com.solaresdedonato.api.adapter.in.utils;

import ar.com.solaresdedonato.api.adapter.in.security.model.JwtAuthenticationDetails;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor
public final class SecurityUtils {

    public static String getCurrentUserId() {
        return getDetails() != null ? getDetails().getUserId() : null;
    }

    public static String getCurrentSystemId() {
        return getDetails() != null ? getDetails().getSystemId() : null;
    }

    private static JwtAuthenticationDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof JwtAuthenticationDetails details) {
            return details;
        }
        return null;
    }
}