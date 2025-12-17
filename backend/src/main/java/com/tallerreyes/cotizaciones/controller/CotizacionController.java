package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.CotizacionPieza;
import com.tallerreyes.cotizaciones.model.CotizacionServicio;
import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import com.tallerreyes.cotizaciones.service.CotizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/cotizaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    /**
     * RF-12, RF-15: Crear cotización (Cotizador)
     * POST /api/cotizaciones
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> crear(@RequestBody Cotizacion cotizacion) {
        try {
            Cotizacion creada = cotizacionService.crearCotizacion(cotizacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-13: Agregar pieza a cotización
     * POST /api/cotizaciones/{id}/piezas
     */
    @PostMapping("/{id}/piezas")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> agregarPieza(@PathVariable Long id, @RequestBody CotizacionPieza pieza) {
        try {
            Cotizacion actualizada = cotizacionService.agregarPieza(id, pieza);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-26: Actualizar descuento de pieza
     * PATCH /api/cotizaciones/{idCotizacion}/piezas/{idPieza}/descuento
     */
    @PatchMapping("/{idCotizacion}/piezas/{idPieza}/descuento")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> actualizarDescuentoPieza(
            @PathVariable Long idCotizacion,
            @PathVariable Long idPieza,
            @RequestBody Map<String, BigDecimal> body
    ) {
        try {
            BigDecimal nuevoDescuento = body.get("descuento");
            Cotizacion actualizada = cotizacionService.actualizarDescuentoPieza(idCotizacion, idPieza, nuevoDescuento);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-12: Agregar servicio (mano de obra)
     * POST /api/cotizaciones/{id}/servicios
     */
    @PostMapping("/{id}/servicios")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> agregarServicio(@PathVariable Long id, @RequestBody CotizacionServicio servicio) {
        try {
            Cotizacion actualizada = cotizacionService.agregarServicio(id, servicio);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-18: Actualizar estado (Administrador)
     * PATCH /api/cotizaciones/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            EstadoCotizacion nuevoEstado = EstadoCotizacion.valueOf(body.get("estado"));
            Cotizacion actualizada = cotizacionService.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-19, RF-20: Obtener cotización por ID
     * GET /api/cotizaciones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Cotizacion cotizacion = cotizacionService.obtenerPorId(id);
            return ResponseEntity.ok(cotizacion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * RF-19: Listar todas las cotizaciones
     * GET /api/cotizaciones
     */
    @GetMapping
    public ResponseEntity<List<Cotizacion>> listarTodas() {
        return ResponseEntity.ok(cotizacionService.listarTodas());
    }

    /**
     * RF-25: Filtrar por cliente
     * GET /api/cotizaciones/cliente/{idCliente}
     */
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Cotizacion>> listarPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(cotizacionService.listarPorCliente(idCliente));
    }

    /**
     * RF-25: Filtrar por estado
     * GET /api/cotizaciones/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cotizacion>> listarPorEstado(@PathVariable EstadoCotizacion estado) {
        return ResponseEntity.ok(cotizacionService.listarPorEstado(estado));
    }

    /**
     * RF-25: Filtrar por usuario (empleado)
     * GET /api/cotizaciones/usuario/{idUsuario}
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Cotizacion>> listarPorUsuario(@PathVariable UUID idUsuario) {
        return ResponseEntity.ok(cotizacionService.listarPorUsuario(idUsuario));
    }

    /**
     * RF-25: Filtrar por rango de fechas
     * GET /api/cotizaciones/fechas?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Cotizacion>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(cotizacionService.listarPorFechas(inicio, fin));
    }
}


