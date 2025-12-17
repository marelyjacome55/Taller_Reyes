package com.tallerreyes.cotizaciones.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    /**
     * Extrae el userId (sub claim) del token JWT de Supabase
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el rol/puesto del usuario del token
     * En Supabase puedes almacenar metadata en el token (app_metadata o user_metadata)
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        // Ajusta según cómo guardas el rol en Supabase
        // Ejemplo: claims.get("app_metadata", Map.class).get("role")
        Object role = claims.get("role");
        return role != null ? role.toString() : "Cotizador"; // default
    }

    /**
     * Valida si el token no ha expirado
     */
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
