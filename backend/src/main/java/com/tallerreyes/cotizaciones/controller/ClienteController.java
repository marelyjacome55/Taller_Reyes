package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Cliente;
import com.tallerreyes.cotizaciones.model.enums.TipoCliente;
import com.tallerreyes.cotizaciones.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * RF-04: Registrar cliente (Administrador, Cotizador)
     * POST /api/clientes
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('Administrador', 'Cotizador')")
    public ResponseEntity<?> registrar(@RequestBody Cliente cliente) {
        try {
            Cliente creado = clienteService.registrarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-05: Editar cliente (solo Administrador)
     * PUT /api/clientes/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente actualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener cliente por ID
     * GET /api/clientes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.obtenerPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * RF-08: Listar todos los clientes
     * GET /api/clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    /**
     * RF-08: Filtrar por estatus
     * GET /api/clientes/estatus/{estatus}
     */
    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Cliente>> listarPorEstatus(@PathVariable Boolean estatus) {
        return ResponseEntity.ok(clienteService.listarPorEstatus(estatus));
    }

    /**
     * RF-08: Filtrar por tipo
     * GET /api/clientes/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Cliente>> listarPorTipo(@PathVariable TipoCliente tipo) {
        return ResponseEntity.ok(clienteService.listarPorTipo(tipo));
    }

    /**
     * RF-08: Filtrar por rango de fechas
     * GET /api/clientes/fechas?inicio=2024-01-01&fin=2024-12-31
     */
    @GetMapping("/fechas")
    public ResponseEntity<List<Cliente>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin
    ) {
        return ResponseEntity.ok(clienteService.listarPorFechas(inicio, fin));
    }

    /**
     * Buscar por nombre
     * GET /api/clientes/buscar?nombre=Juan
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(clienteService.buscarPorNombre(nombre));
    }

    /**
     * Cambiar estatus (activar/desactivar)
     * PATCH /api/clientes/{id}/estatus
     */
    @PatchMapping("/{id}/estatus")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> cambiarEstatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        try {
            Boolean nuevoEstatus = body.get("estatus");
            Cliente actualizado = clienteService.cambiarEstatus(id, nuevoEstatus);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
