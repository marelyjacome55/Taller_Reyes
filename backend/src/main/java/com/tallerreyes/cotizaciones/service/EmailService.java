package com.tallerreyes.cotizaciones.service;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.EnvioCorreo;
import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import com.tallerreyes.cotizaciones.repository.EnvioCorreoRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender mailSender;
    private final EnvioCorreoRepository envioCorreoRepository;
    private final PDFService pdfService;

    @Value("${spring.mail.username}")
    private String correoOrigen;

    /**
     * RF-17: Enviar cotización por correo
     * RNF-14: Registrar envío en BD
     */
    public EnvioCorreo enviarCotizacion(
            Cotizacion cotizacion,
            String correoDestino,
            String asunto,
            String cuerpoMensaje
    ) {
        EnvioCorreo envio = new EnvioCorreo();
        envio.setCotizacion(cotizacion);
        envio.setCorreoOrigen(correoOrigen);
        envio.setCorreoDestino(correoDestino);
        envio.setAsunto(asunto);
        envio.setCuerpo(cuerpoMensaje);
        envio.setFechaEnvio(LocalDateTime.now());

        try {
            // Generar PDF de la cotización
            byte[] pdfBytes = pdfService.generarPDFCotizacion(cotizacion);

            // Crear mensaje
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(correoOrigen);
            helper.setTo(correoDestino);
            helper.setSubject(asunto);
            helper.setText(cuerpoMensaje, true); // true = HTML

            // Adjuntar PDF
            String nombreArchivo = String.format("Cotizacion_%s.pdf", 
                cotizacion.getFolio() != null ? cotizacion.getFolio() : cotizacion.getIdCotizaciones());
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdfBytes));

            // Enviar
            mailSender.send(message);

            // Marcar como exitoso
            envio.setExito(true);
            envio.setPdfUrl(nombreArchivo); // O guardar en cloud storage

        } catch (Exception e) {
            // RNF-14: Registrar error
            envio.setExito(false);
            envio.setMensajeError(e.getMessage());
        }

        // RNF-14: Guardar en BD siempre (éxito o error)
        return envioCorreoRepository.save(envio);
    }

    /**
     * RF-18: Registrar respuesta del cliente
     */
    public EnvioCorreo registrarRespuestaCliente(
            Long idEnvio,
            String respuestaCliente,
            EstadoCotizacion estadoResultante
    ) {
        EnvioCorreo envio = envioCorreoRepository.findById(idEnvio)
                .orElseThrow(() -> new IllegalArgumentException("Envío no encontrado"));

        envio.setRespuestaCliente(respuestaCliente);
        envio.setFechaRespuesta(LocalDateTime.now());
        envio.setEstadoResultante(estadoResultante);

        return envioCorreoRepository.save(envio);
    }

    /**
     * Obtener historial de envíos de una cotización
     */
    @Transactional(readOnly = true)
    public List<EnvioCorreo> obtenerEnviosDeCotizacion(Long idCotizacion) {
        return envioCorreoRepository.findByCotizacion_IdCotizaciones(idCotizacion);
    }

    /**
     * Obtener envíos fallidos para reintento
     */
    @Transactional(readOnly = true)
    public List<EnvioCorreo> obtenerEnviosFallidos() {
        return envioCorreoRepository.findByExito(false);
    }

    /**
     * Reenviar correo fallido
     */
    public EnvioCorreo reenviarCorreo(Long idEnvio) {
        EnvioCorreo envioOriginal = envioCorreoRepository.findById(idEnvio)
                .orElseThrow(() -> new IllegalArgumentException("Envío no encontrado"));

        return enviarCotizacion(
            envioOriginal.getCotizacion(),
            envioOriginal.getCorreoDestino(),
            envioOriginal.getAsunto(),
            envioOriginal.getCuerpo()
        );
    }
}
