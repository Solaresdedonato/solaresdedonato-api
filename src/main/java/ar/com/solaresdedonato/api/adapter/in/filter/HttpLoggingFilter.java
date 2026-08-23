package ar.com.solaresdedonato.api.adapter.in.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestResponse(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestResponse(ContentCachingRequestWrapper request,
                                    ContentCachingResponseWrapper response,
                                    long duration) {
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("HTTP Request/Response {}{}{}{}{}{}{}",
                kv("method", request.getMethod()),
                kv("path", request.getRequestURI()),
                kv("status", response.getStatus()),
                kv("duration_ms", duration),
                kv("request_size", request.getContentLength()),
                kv("response_size", response.getContentSize()),
                kv("response_body", responseBody.length() > 1000 ? "[TRUNCATED]" : responseBody)
        );
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || path.startsWith("/health");
    }
}