package com.tallerreyes.cotizaciones.controller;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.service.CotizacionService;
import com.tallerreyes.cotizaciones.service.PDFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PDFController {

    private final PDFService pdfService;
    private final CotizacionService cotizacionService;

    /**
     * RF-16: Generar PDF de cotización
     * RNF-19: Generación rápida
     * GET /api/pdf/cotizacion/{id}
     */
    @GetMapping("/cotizacion/{id}")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador', 'Contador')")
    public ResponseEntity<byte[]> generarPDF(@PathVariable Long id) {
        try {
            Cotizacion cotizacion = cotizacionService.obtenerPorId(id);
            byte[] pdfBytes = pdfService.generarPDFCotizacion(cotizacion);

            String filename = String.format("Cotizacion_%s.pdf", 
                cotizacion.getFolio() != null ? cotizacion.getFolio() : cotizacion.getIdCotizaciones());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Previsualizar PDF (inline en navegador)
     * GET /api/pdf/cotizacion/{id}/preview
     */
    @GetMapping("/cotizacion/{id}/preview")
    @PreAuthorize("hasAnyRole('Cotizador', 'Administrador', 'Contador')")
    public ResponseEntity<byte[]> previsualizarPDF(@PathVariable Long id) {
        try {
            Cotizacion cotizacion = cotizacionService.obtenerPorId(id);
            byte[] pdfBytes = pdfService.generarPDFCotizacion(cotizacion);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "preview.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
