package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Usuario;
import com.tallerreyes.cotizaciones.model.enums.EstatusUsuario;
import com.tallerreyes.cotizaciones.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * RF-01, RF-06: Registrar empleado (solo Administrador)
     * POST /api/usuarios
     */
    @PostMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-07: Editar empleado (solo Administrador)
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> actualizar(@PathVariable UUID id, @RequestBody Usuario usuario) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-24: Cambiar estatus (solo Administrador)
     * PATCH /api/usuarios/{id}/estatus
     */
    @PatchMapping("/{id}/estatus")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> cambiarEstatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        try {
            EstatusUsuario nuevoEstatus = EstatusUsuario.valueOf(body.get("estatus"));
            Usuario actualizado = usuarioService.cambiarEstatus(id, nuevoEstatus);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            Usuario usuario = usuarioService.obtenerPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Listar todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Filtrar por estatus
     * GET /api/usuarios/estatus/{estatus}
     */
    @GetMapping("/estatus/{estatus}")
    public ResponseEntity<List<Usuario>> listarPorEstatus(@PathVariable EstatusUsuario estatus) {
        return ResponseEntity.ok(usuarioService.listarPorEstatus(estatus));
    }

    /**
     * Filtrar por puesto/rol
     * GET /api/usuarios/puesto/{puesto}
     */
    @GetMapping("/puesto/{puesto}")
    public ResponseEntity<List<Usuario>> listarPorPuesto(@PathVariable String puesto) {
        return ResponseEntity.ok(usuarioService.listarPorPuesto(puesto));
    }
}
