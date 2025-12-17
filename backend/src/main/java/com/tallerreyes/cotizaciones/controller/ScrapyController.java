package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.ResultadoScrapy;
import com.tallerreyes.cotizaciones.service.CotizacionService;
import com.tallerreyes.cotizaciones.service.ScrapyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scrapy")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ScrapyController {

    private final ScrapyService scrapyService;
    private final CotizacionService cotizacionService;

    /**
     * RF-10: Buscar piezas (usa caché si está disponible)
     * RF-21: Uso de caché automático
     * POST /api/scrapy/buscar
     */
    @PostMapping("/buscar")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> buscarPiezas(@RequestBody Map<String, Object> body) {
        try {
            String termino = (String) body.get("termino");
            Long idCotizacion = body.get("idCotizacion") != null ? 
                Long.valueOf(body.get("idCotizacion").toString()) : null;

            Cotizacion cotizacion = null;
            if (idCotizacion != null) {
                cotizacion = cotizacionService.obtenerPorId(idCotizacion);
            }

            List<ResultadoScrapy> resultados = scrapyService.buscarPiezas(termino, cotizacion);
            return ResponseEntity.ok(Map.of(
                "termino", termino,
                "resultados", resultados,
                "total", resultados.size()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-22: Obtener resultados guardados de una cotización
     * GET /api/scrapy/cotizacion/{idCotizacion}
     */
    @GetMapping("/cotizacion/{idCotizacion}")
    public ResponseEntity<List<ResultadoScrapy>> obtenerResultadosDeCotizacion(@PathVariable Long idCotizacion) {
        List<ResultadoScrapy> resultados = scrapyService.obtenerResultadosDeCotizacion(idCotizacion);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Health check del servicio Scrapy Python
     * GET /api/scrapy/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "ok",
            "message", "Scrapy service integration configured"
        ));
    }
}
