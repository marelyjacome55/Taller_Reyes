package com.tallerreyes.cotizaciones.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.CotizacionPieza;
import com.tallerreyes.cotizaciones.model.CotizacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PDFService {

    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 8);

    /**
     * RF-16: Generar PDF de cotización completa
     * RNF-19: Generación rápida sin afectar UX
     */
    public byte[] generarPDFCotizacion(Cotizacion cotizacion) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Encabezado
            agregarEncabezado(document, cotizacion);
            document.add(new Paragraph(" ")); // Espacio

            // Información del cliente
            agregarInfoCliente(document, cotizacion);
            document.add(new Paragraph(" "));

            // Información del vehículo
            agregarInfoVehiculo(document, cotizacion);
            document.add(new Paragraph(" "));

            // Tabla de piezas
            if (cotizacion.getPiezas() != null && !cotizacion.getPiezas().isEmpty()) {
                agregarTablaPiezas(document, cotizacion);
                document.add(new Paragraph(" "));
            }

            // Tabla de servicios (mano de obra)
            if (cotizacion.getServicios() != null && !cotizacion.getServicios().isEmpty()) {
                agregarTablaServicios(document, cotizacion);
                document.add(new Paragraph(" "));
            }

            // Totales
            agregarTotales(document, cotizacion);

            // Pie de página
            agregarPiePagina(document, cotizacion);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }

    private void agregarEncabezado(Document document, Cotizacion cotizacion) throws DocumentException {
        Paragraph title = new Paragraph("COTIZACIÓN", FONT_TITLE);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        if (cotizacion.getFolio() != null) {
            Paragraph folio = new Paragraph("Folio: " + cotizacion.getFolio(), FONT_SUBTITLE);
            folio.setAlignment(Element.ALIGN_CENTER);
            document.add(folio);
        }

        if (cotizacion.getFecha() != null) {
            String fecha = cotizacion.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph fechaP = new Paragraph("Fecha: " + fecha, FONT_NORMAL);
            fechaP.setAlignment(Element.ALIGN_RIGHT);
            document.add(fechaP);
        }

        if (cotizacion.getEncabezado() != null && !cotizacion.getEncabezado().isEmpty()) {
            document.add(new Paragraph(" "));
            Paragraph encabezado = new Paragraph(cotizacion.getEncabezado(), FONT_NORMAL);
            document.add(encabezado);
        }
    }

    private void agregarInfoCliente(Document document, Cotizacion cotizacion) throws DocumentException {
        if (cotizacion.getCliente() == null) return;

        Paragraph titulo = new Paragraph("INFORMACIÓN DEL CLIENTE", FONT_SUBTITLE);
        document.add(titulo);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);

        agregarCeldaInfo(table, "Nombre:", cotizacion.getCliente().getNombre());
        agregarCeldaInfo(table, "Correo:", cotizacion.getCliente().getCorreo());
        agregarCeldaInfo(table, "Teléfono:", cotizacion.getCliente().getTelefono());
        agregarCeldaInfo(table, "RFC:", cotizacion.getCliente().getRfc());

        document.add(table);
    }

    private void agregarInfoVehiculo(Document document, Cotizacion cotizacion) throws DocumentException {
        Paragraph titulo = new Paragraph("INFORMACIÓN DEL VEHÍCULO", FONT_SUBTITLE);
        document.add(titulo);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);

        agregarCeldaInfo(table, "Marca:", cotizacion.getMarca());
        agregarCeldaInfo(table, "Modelo:", cotizacion.getModelo());
        agregarCeldaInfo(table, "Año:", cotizacion.getAnio());
        if (cotizacion.getVersion() != null) {
            agregarCeldaInfo(table, "Versión:", cotizacion.getVersion());
        }
        if (cotizacion.getMotor() != null) {
            agregarCeldaInfo(table, "Motor:", cotizacion.getMotor());
        }

        document.add(table);
    }

    private void agregarTablaPiezas(Document document, Cotizacion cotizacion) throws DocumentException {
        Paragraph titulo = new Paragraph("PIEZAS Y REFACCIONES", FONT_SUBTITLE);
        document.add(titulo);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setWidths(new float[]{3f, 1f, 1f, 1.5f, 1f, 1f, 1.5f});

        // Encabezados
        agregarCeldaEncabezado(table, "Descripción");
        agregarCeldaEncabezado(table, "Cant.");
        agregarCeldaEncabezado(table, "Unidad");
        agregarCeldaEncabezado(table, "Precio Unit.");
        agregarCeldaEncabezado(table, "Desc %");
        agregarCeldaEncabezado(table, "IVA");
        agregarCeldaEncabezado(table, "Total");

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        for (CotizacionPieza pieza : cotizacion.getPiezas()) {
            agregarCeldaDato(table, pieza.getNombre());
            agregarCeldaDato(table, String.valueOf(pieza.getCantidad()));
            agregarCeldaDato(table, pieza.getUnidad());
            agregarCeldaDato(table, formatearMoneda(pieza.getPrecioUnitario(), currencyFormat));
            agregarCeldaDato(table, formatearPorcentaje(pieza.getDescuento()));
            agregarCeldaDato(table, formatearMoneda(pieza.getIva(), currencyFormat));
            agregarCeldaDato(table, formatearMoneda(pieza.getTotalConImpuestos(), currencyFormat));
        }

        document.add(table);
    }

    private void agregarTablaServicios(Document document, Cotizacion cotizacion) throws DocumentException {
        Paragraph titulo = new Paragraph("SERVICIOS Y MANO DE OBRA", FONT_SUBTITLE);
        document.add(titulo);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setWidths(new float[]{4f, 4f, 2f});

        agregarCeldaEncabezado(table, "Servicio");
        agregarCeldaEncabezado(table, "Descripción");
        agregarCeldaEncabezado(table, "Monto");

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        for (CotizacionServicio servicio : cotizacion.getServicios()) {
            agregarCeldaDato(table, servicio.getNombre());
            agregarCeldaDato(table, servicio.getDescripcion());
            agregarCeldaDato(table, formatearMoneda(servicio.getMonto(), currencyFormat));
        }

        document.add(table);
    }

    private void agregarTotales(Document document, Cotizacion cotizacion) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setSpacingBefore(10);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        agregarCeldaTotal(table, "Subtotal:", formatearMoneda(cotizacion.getSubtotal(), currencyFormat));
        agregarCeldaTotal(table, "Descuento:", formatearMoneda(cotizacion.getDescuentoTotal(), currencyFormat));
        agregarCeldaTotal(table, "IVA (16%):", formatearMoneda(cotizacion.getIvaTotal(), currencyFormat));
        agregarCeldaTotal(table, "Retención IVA:", formatearMoneda(cotizacion.getRetencionIvaTotal(), currencyFormat));
        
        PdfPCell cellLabel = new PdfPCell(new Phrase("GRAN TOTAL:", FONT_SUBTITLE));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(formatearMoneda(cotizacion.getGranTotal(), currencyFormat), FONT_SUBTITLE));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);

        document.add(table);
    }

    private void agregarPiePagina(Document document, Cotizacion cotizacion) throws DocumentException {
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        if (cotizacion.getMetodoPago() != null || cotizacion.getFormaPago() != null) {
            Paragraph pago = new Paragraph("Forma de Pago: ", FONT_SMALL);
            if (cotizacion.getMetodoPago() != null) {
                pago.add(new Chunk(cotizacion.getMetodoPago().toString(), FONT_NORMAL));
            }
            if (cotizacion.getFormaPago() != null) {
                pago.add(new Chunk(" - " + cotizacion.getFormaPago().toString(), FONT_NORMAL));
            }
            document.add(pago);
        }

        Paragraph nota = new Paragraph("Esta cotización tiene una vigencia de 15 días.", FONT_SMALL);
        nota.setAlignment(Element.ALIGN_CENTER);
        document.add(nota);
    }

    // Métodos auxiliares
    private void agregarCeldaInfo(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, FONT_SUBTITLE));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value != null ? value : "", FONT_NORMAL));
        cellValue.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellValue);
    }

    private void agregarCeldaEncabezado(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_SUBTITLE));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void agregarCeldaDato(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", FONT_SMALL));
        cell.setPadding(3);
        table.addCell(cell);
    }

    private void agregarCeldaTotal(PdfPTable table, String label, String value) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, FONT_NORMAL));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, FONT_NORMAL));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);
    }

    private String formatearMoneda(BigDecimal valor, NumberFormat format) {
        return valor != null ? format.format(valor) : "$0.00";
    }

    private String formatearPorcentaje(BigDecimal valor) {
        return valor != null ? valor.toString() + "%" : "0%";
    }
}
