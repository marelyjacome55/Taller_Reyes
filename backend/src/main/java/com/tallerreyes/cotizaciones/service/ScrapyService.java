package com.tallerreyes.cotizaciones.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerreyes.cotizaciones.model.CacheScrapy;
import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.ResultadoScrapy;
import com.tallerreyes.cotizaciones.repository.CacheScrapyRepository;
import com.tallerreyes.cotizaciones.repository.ResultadoScrapyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapyService {

    private final CacheScrapyRepository cacheRepository;
    private final ResultadoScrapyRepository resultadoRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${scrapy.service.url:http://localhost:8001}")
    private String scrapyServiceUrl;

    @Value("${scrapy.cache.ttl.hours:24}")
    private int cacheTtlHours;

    /**
     * RF-10: Buscar piezas (primero en caché, luego en scrapy)
     * RF-21: Uso de caché
     */
    public List<ResultadoScrapy> buscarPiezas(String termino, Cotizacion cotizacion) {
        // RF-21: Intentar obtener de caché
        Optional<CacheScrapy> cache = obtenerCacheValido(termino);
        
        if (cache.isPresent()) {
            // Retornar resultados desde caché (parsear JSON)
            return parsearResultadosDesdeCache(cache.get(), cotizacion);
        }

        // Si no hay caché, hacer búsqueda real en Python scrapy
        return buscarEnScrapyYGuardarCache(termino, cotizacion);
    }

    /**
     * RF-21: Obtener caché válido (últimas N horas)
     */
    private Optional<CacheScrapy> obtenerCacheValido(String termino) {
        LocalDateTime fechaMinima = LocalDateTime.now().minusHours(cacheTtlHours);
        return cacheRepository.findByTerminoAndFechaAfter(termino, fechaMinima);
    }

    /**
     * Buscar en servicio Python scrapy y guardar en caché
     */
    private List<ResultadoScrapy> buscarEnScrapyYGuardarCache(String termino, Cotizacion cotizacion) {
        try {
            // Llamar al servicio Python scrapy
            String url = scrapyServiceUrl + "/api/buscar";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            String requestBody = String.format("{\"termino\": \"%s\"}", termino);
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                request, 
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String jsonResultados = response.getBody();
                
                // RF-21: Guardar en caché
                guardarEnCache(termino, jsonResultados);
                
                // RF-22: Parsear y guardar resultados individuales
                return parsearYGuardarResultados(jsonResultados, termino, cotizacion);
            }

        } catch (Exception e) {
            // Log error pero no fallar (RNF-18: tolerante a fallos)
            System.err.println("Error al buscar en scrapy: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * RF-21: Guardar respuesta en caché
     */
    private void guardarEnCache(String termino, String jsonData) {
        CacheScrapy cache = new CacheScrapy();
        cache.setTermino(termino);
        cache.setDataJson(jsonData);
        cache.setFecha(LocalDateTime.now());
        cacheRepository.save(cache);
    }

    /**
     * RF-22: Parsear JSON y guardar resultados en BD
     */
    private List<ResultadoScrapy> parsearYGuardarResultados(
            String jsonData, 
            String termino, 
            Cotizacion cotizacion
    ) {
        List<ResultadoScrapy> resultados = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonData);
            JsonNode resultadosNode = root.get("resultados");

            if (resultadosNode != null && resultadosNode.isArray()) {
                for (JsonNode item : resultadosNode) {
                    ResultadoScrapy resultado = new ResultadoScrapy();
                    resultado.setCotizacion(cotizacion);
                    resultado.setTerminoBusqueda(termino);
                    resultado.setFuente(item.has("fuente") ? item.get("fuente").asText() : "");
                    resultado.setTitulo(item.has("titulo") ? item.get("titulo").asText() : "");
                    resultado.setPrecio(
                        item.has("precio") ? 
                        new BigDecimal(item.get("precio").asText().replaceAll("[^0-9.]", "")) : 
                        BigDecimal.ZERO
                    );
                    resultado.setUrl(item.has("url") ? item.get("url").asText() : "");
                    resultado.setFecha(LocalDateTime.now());

                    resultados.add(resultado);
                }

                // Guardar todos los resultados
                if (cotizacion != null && cotizacion.getIdCotizaciones() != null) {
                    resultadoRepository.saveAll(resultados);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al parsear resultados scrapy: " + e.getMessage());
        }

        return resultados;
    }

    /**
     * Parsear resultados desde caché existente
     */
    private List<ResultadoScrapy> parsearResultadosDesdeCache(
            CacheScrapy cache, 
            Cotizacion cotizacion
    ) {
        return parsearYGuardarResultados(cache.getDataJson(), cache.getTermino(), cotizacion);
    }

    /**
     * Obtener resultados guardados de una cotización
     */
    @Transactional(readOnly = true)
    public List<ResultadoScrapy> obtenerResultadosDeCotizacion(Long idCotizacion) {
        return resultadoRepository.findByCotizacion_IdCotizaciones(idCotizacion);
    }

    /**
     * Limpiar caché antiguo (puede ser un scheduled task)
     */
    public void limpiarCacheAntiguo() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(cacheTtlHours * 2);
        // Implementar query personalizado si es necesario
    }
}
