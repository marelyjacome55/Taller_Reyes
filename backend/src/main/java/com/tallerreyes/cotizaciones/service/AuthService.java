package com.tallerreyes.cotizaciones.service;

import com.tallerreyes.cotizaciones.model.Usuario;
import com.tallerreyes.cotizaciones.repository.UsuarioRepository;
import com.tallerreyes.cotizaciones.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon.key}")
    private String supabaseAnonKey;

    /**
     * RF-02: Iniciar sesión con Supabase Auth
     * Supabase maneja autenticación, nosotros solo validamos token
     */
    public Map<String, Object> login(String correo, String password) {
        try {
            // Llamar a Supabase Auth API
            String url = supabaseUrl + "/auth/v1/token?grant_type=password";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", supabaseAnonKey);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("email", correo);
            requestBody.put("password", password);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> authResponse = response.getBody();
                String accessToken = (String) authResponse.get("access_token");
                String refreshToken = (String) authResponse.get("refresh_token");

                // Extraer userId del token
                String userId = jwtService.extractUserId(accessToken);
                
                // Buscar usuario en nuestra BD
                Usuario usuario = usuarioRepository.findById(UUID.fromString(userId))
                        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en BD local"));

                // Extraer rol
                String role = usuario.getPuesto() != null ? 
                    usuario.getPuesto().getNombre().toString() : "Cotizador";

                // Retornar datos de sesión
                Map<String, Object> resultado = new HashMap<>();
                resultado.put("accessToken", accessToken);
                resultado.put("refreshToken", refreshToken);
                resultado.put("usuario", usuario);
                resultado.put("role", role);

                return resultado;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage());
        }

        throw new RuntimeException("Credenciales inválidas");
    }

    /**
     * RF-03: Cerrar sesión (Supabase invalida token)
     */
    public void logout(String accessToken) {
        try {
            String url = supabaseUrl + "/auth/v1/logout";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseAnonKey);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<Void> request = new HttpEntity<>(headers);
            
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Void.class
            );

        } catch (Exception e) {
            // Log pero no fallar
            System.err.println("Error al cerrar sesión en Supabase: " + e.getMessage());
        }
    }

    /**
     * Refresh token (renovar sesión)
     */
    public Map<String, Object> refreshToken(String refreshToken) {
        try {
            String url = supabaseUrl + "/auth/v1/token?grant_type=refresh_token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apikey", supabaseAnonKey);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("refresh_token", refreshToken);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                (Class<Map<String, Object>>) (Class<?>) Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al renovar token: " + e.getMessage());
        }

        throw new RuntimeException("No se pudo renovar el token");
    }

    /**
     * Validar si un token es válido
     */
    public boolean validarToken(String token) {
        return jwtService.isTokenValid(token);
    }

    /**
     * Extraer información del usuario desde el token
     */
    public Usuario obtenerUsuarioDesdeToken(String token) {
        String userId = jwtService.extractUserId(token);
        return usuarioRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
