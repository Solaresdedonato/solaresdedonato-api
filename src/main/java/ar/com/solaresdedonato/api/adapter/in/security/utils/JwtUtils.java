package ar.com.solaresdedonato.api.adapter.in.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final long EXPIRATION_MS = 8L * 60 * 60 * 1000; // 8 horas

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String email, String userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public String extractSystemId(String token) {
        return extractClaim(token, claims -> claims.get("systemId", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenValid(String token) {
        try {
            return !extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}