package ar.com.solaresdedonato.api.adapter.in.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LogMdcFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString();
            }
            MDC.put("request_id", requestId);
            MDC.put("method", httpRequest.getMethod());
            MDC.put("path", httpRequest.getRequestURI());
            MDC.put("client_ip", getClientIP(httpRequest));
            httpResponse.setHeader(REQUEST_ID_HEADER, requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}