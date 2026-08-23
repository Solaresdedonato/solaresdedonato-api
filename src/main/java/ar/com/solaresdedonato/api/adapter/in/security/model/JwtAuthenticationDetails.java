package ar.com.solaresdedonato.api.adapter.in.security.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
@Getter
public class JwtAuthenticationDetails extends WebAuthenticationDetails {

    private final String userId;
    private final String systemId;

    public JwtAuthenticationDetails(HttpServletRequest request, String userId, String systemId) {
        super(request);
        this.userId = userId;
        this.systemId = systemId;
    }
}