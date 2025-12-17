package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.EnvioCorreo;
import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import com.tallerreyes.cotizaciones.service.CotizacionService;
import com.tallerreyes.cotizaciones.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;
    private final CotizacionService cotizacionService;

    /**
     * RF-17: Enviar cotización por correo
     * POST /api/email/enviar
     */
    @PostMapping("/enviar")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> enviarCotizacion(@RequestBody Map<String, Object> body) {
        try {
            Long idCotizacion = Long.valueOf(body.get("idCotizacion").toString());
            String correoDestino = (String) body.get("correoDestino");
            String asunto = (String) body.get("asunto");
            String cuerpo = (String) body.get("cuerpo");

            Cotizacion cotizacion = cotizacionService.obtenerPorId(idCotizacion);
            EnvioCorreo envio = emailService.enviarCotizacion(cotizacion, correoDestino, asunto, cuerpo);

            if (envio.getExito()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Correo enviado exitosamente",
                    "envio", envio
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error al enviar correo",
                    "error", envio.getMensajeError(),
                    "envio", envio
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * RF-18: Registrar respuesta del cliente
     * POST /api/email/{idEnvio}/respuesta
     */
    @PostMapping("/{idEnvio}/respuesta")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<?> registrarRespuesta(
            @PathVariable Long idEnvio,
            @RequestBody Map<String, String> body
    ) {
        try {
            String respuesta = body.get("respuesta");
            EstadoCotizacion estado = EstadoCotizacion.valueOf(body.get("estado"));

            EnvioCorreo envio = emailService.registrarRespuestaCliente(idEnvio, respuesta, estado);
            
            // Actualizar estado de la cotización
            if (envio.getCotizacion() != null) {
                cotizacionService.actualizarEstado(envio.getCotizacion().getIdCotizaciones(), estado);
            }

            return ResponseEntity.ok(envio);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener historial de envíos de una cotización
     * GET /api/email/cotizacion/{idCotizacion}
     */
    @GetMapping("/cotizacion/{idCotizacion}")
    public ResponseEntity<List<EnvioCorreo>> obtenerEnviosDeCotizacion(@PathVariable Long idCotizacion) {
        List<EnvioCorreo> envios = emailService.obtenerEnviosDeCotizacion(idCotizacion);
        return ResponseEntity.ok(envios);
    }

    /**
     * RNF-14: Obtener envíos fallidos para revisión
     * GET /api/email/fallidos
     */
    @GetMapping("/fallidos")
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<List<EnvioCorreo>> obtenerEnviosFallidos() {
        List<EnvioCorreo> fallidos = emailService.obtenerEnviosFallidos();
        return ResponseEntity.ok(fallidos);
    }

    /**
     * Reenviar correo fallido
     * POST /api/email/{idEnvio}/reenviar
     */
    @PostMapping("/{idEnvio}/reenviar")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador')")
    public ResponseEntity<?> reenviarCorreo(@PathVariable Long idEnvio) {
        try {
            EnvioCorreo nuevoEnvio = emailService.reenviarCorreo(idEnvio);
            
            if (nuevoEnvio.getExito()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Correo reenviado exitosamente",
                    "envio", nuevoEnvio
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Error al reenviar correo",
                    "error", nuevoEnvio.getMensajeError()
                ));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
