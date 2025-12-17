package com.tallerreyes.cotizaciones.service;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.CotizacionPieza;
import com.tallerreyes.cotizaciones.model.CotizacionServicio;
import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import com.tallerreyes.cotizaciones.repository.CotizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;

    /**
     * RF-12, RF-15: Crear cotización completa con piezas y servicios
     * RF-12.1: Validar que marca, modelo y año sean obligatorios
     */
    public Cotizacion crearCotizacion(Cotizacion cotizacion) {
        // RF-12.1: Validación de vehículo
        validarDatosVehiculo(cotizacion);

        // Establecer fecha si no viene
        if (cotizacion.getFecha() == null) {
            cotizacion.setFecha(LocalDate.now());
        }

        // RF-14: Calcular totales
        recalcularTotales(cotizacion);

        // Asociar piezas y servicios a la cotización (relación bidireccional)
        if (cotizacion.getPiezas() != null) {
            cotizacion.getPiezas().forEach(pieza -> pieza.setCotizacion(cotizacion));
        }
        if (cotizacion.getServicios() != null) {
            cotizacion.getServicios().forEach(servicio -> servicio.setCotizacion(cotizacion));
        }

        return cotizacionRepository.save(cotizacion);
    }

    /**
     * RF-13, RF-26: Agregar pieza a cotización existente
     * RF-14: Recalcular totales automáticamente
     */
    public Cotizacion agregarPieza(Long idCotizacion, CotizacionPieza pieza) {
        Cotizacion cotizacion = obtenerPorId(idCotizacion);
        
        // Calcular subtotal y total de la pieza
        calcularTotalesPieza(pieza);
        
        pieza.setCotizacion(cotizacion);
        cotizacion.getPiezas().add(pieza);

        // RF-14: Recalcular totales de la cotización
        recalcularTotales(cotizacion);

        return cotizacionRepository.save(cotizacion);
    }

    /**
     * RF-26: Editar descuento de pieza y recalcular
     */
    public Cotizacion actualizarDescuentoPieza(Long idCotizacion, Long idPieza, BigDecimal nuevoDescuento) {
        Cotizacion cotizacion = obtenerPorId(idCotizacion);
        
        CotizacionPieza pieza = cotizacion.getPiezas().stream()
                .filter(p -> p.getIdCotizacionPiezas().equals(idPieza))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pieza no encontrada"));

        // Validar descuento (0-100%)
        if (nuevoDescuento.compareTo(BigDecimal.ZERO) < 0 || 
            nuevoDescuento.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Descuento debe estar entre 0 y 100");
        }

        pieza.setDescuento(nuevoDescuento);
        calcularTotalesPieza(pieza);

        // RF-14: Recalcular totales de la cotización
        recalcularTotales(cotizacion);

        return cotizacionRepository.save(cotizacion);
    }

    /**
     * RF-12: Agregar servicio (mano de obra)
     */
    public Cotizacion agregarServicio(Long idCotizacion, CotizacionServicio servicio) {
        Cotizacion cotizacion = obtenerPorId(idCotizacion);
        
        servicio.setCotizacion(cotizacion);
        cotizacion.getServicios().add(servicio);

        // Recalcular totales incluyendo servicios
        recalcularTotales(cotizacion);

        return cotizacionRepository.save(cotizacion);
    }

    /**
     * RF-18: Actualizar estado de cotización (Aprobado/Desaprobado)
     */
    public Cotizacion actualizarEstado(Long idCotizacion, EstadoCotizacion nuevoEstado) {
        Cotizacion cotizacion = obtenerPorId(idCotizacion);
        cotizacion.setEstado(nuevoEstado);
        return cotizacionRepository.save(cotizacion);
    }

    /**
     * RF-19, RF-20: Obtener cotización por ID con detalles completos
     */
    @Transactional(readOnly = true)
    public Cotizacion obtenerPorId(Long idCotizacion) {
        return cotizacionRepository.findById(idCotizacion)
                .orElseThrow(() -> new IllegalArgumentException("Cotización no encontrada"));
    }

    /**
     * RF-19: Listar todas las cotizaciones (ordenadas por fecha desc)
     */
    @Transactional(readOnly = true)
    public List<Cotizacion> listarTodas() {
        return cotizacionRepository.findAllByOrderByFechaDesc();
    }

    /**
     * RF-25: Filtrar por cliente
     */
    @Transactional(readOnly = true)
    public List<Cotizacion> listarPorCliente(Long idCliente) {
        return cotizacionRepository.findByCliente_IdCliente(idCliente);
    }

    /**
     * RF-25: Filtrar por estado
     */
    @Transactional(readOnly = true)
    public List<Cotizacion> listarPorEstado(EstadoCotizacion estado) {
        return cotizacionRepository.findByEstado(estado);
    }

    /**
     * RF-25: Filtrar por usuario (empleado)
     */
    @Transactional(readOnly = true)
    public List<Cotizacion> listarPorUsuario(UUID idUsuario) {
        return cotizacionRepository.findByUsuario_IdUsuario(idUsuario);
    }

    /**
     * RF-25: Filtrar por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Cotizacion> listarPorFechas(LocalDate inicio, LocalDate fin) {
        return cotizacionRepository.findByFechaBetween(inicio, fin);
    }

    // ========== MÉTODOS PRIVADOS DE CÁLCULO ==========

    /**
     * RF-14: Recalcular todos los totales de la cotizacion
     */
    private void recalcularTotales(Cotizacion cotizacion) {
        BigDecimal subtotalPiezas = BigDecimal.ZERO;
        BigDecimal descuentoTotalPiezas = BigDecimal.ZERO;
        BigDecimal ivaTotalPiezas = BigDecimal.ZERO;
        BigDecimal retencionIvaTotalPiezas = BigDecimal.ZERO;
        BigDecimal totalPiezas = BigDecimal.ZERO;

        // Sumar piezas
        if (cotizacion.getPiezas() != null) {
            for (CotizacionPieza pieza : cotizacion.getPiezas()) {
                calcularTotalesPieza(pieza);
                subtotalPiezas = subtotalPiezas.add(pieza.getSubtotal() != null ? pieza.getSubtotal() : BigDecimal.ZERO);
                descuentoTotalPiezas = descuentoTotalPiezas.add(calcularMontoDescuento(pieza));
                ivaTotalPiezas = ivaTotalPiezas.add(pieza.getIva() != null ? pieza.getIva() : BigDecimal.ZERO);
                retencionIvaTotalPiezas = retencionIvaTotalPiezas.add(pieza.getRetencionIva() != null ? pieza.getRetencionIva() : BigDecimal.ZERO);
                totalPiezas = totalPiezas.add(pieza.getTotalConImpuestos() != null ? pieza.getTotalConImpuestos() : BigDecimal.ZERO);
            }
        }

        // Sumar servicios (mano de obra)
        BigDecimal totalServicios = BigDecimal.ZERO;
        if (cotizacion.getServicios() != null) {
            for (CotizacionServicio servicio : cotizacion.getServicios()) {
                totalServicios = totalServicios.add(servicio.getMonto() != null ? servicio.getMonto() : BigDecimal.ZERO);
            }
        }

        // Totales finales
        cotizacion.setSubtotal(subtotalPiezas);
        cotizacion.setDescuentoTotal(descuentoTotalPiezas);
        cotizacion.setIvaTotal(ivaTotalPiezas);
        cotizacion.setRetencionIvaTotal(retencionIvaTotalPiezas);
        cotizacion.setTotal(totalPiezas);
        cotizacion.setGranTotal(totalPiezas.add(totalServicios));
    }

    /**
     * RF-13, RF-26: Calcular totales de una pieza individual
     */
    private void calcularTotalesPieza(CotizacionPieza pieza) {
        if (pieza.getCantidad() == null || pieza.getPrecioUnitario() == null) {
            return;
        }

        BigDecimal cantidad = new BigDecimal(pieza.getCantidad());
        BigDecimal precioUnitario = pieza.getPrecioUnitario();

        // Subtotal = cantidad * precio
        BigDecimal subtotal = cantidad.multiply(precioUnitario).setScale(2, RoundingMode.HALF_UP);

        // Descuento (porcentaje)
        BigDecimal descuento = pieza.getDescuento() != null ? pieza.getDescuento() : BigDecimal.ZERO;
        BigDecimal montoDescuento = subtotal.multiply(descuento).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal subtotalConDescuento = subtotal.subtract(montoDescuento);

        // IVA (16% sobre subtotal con descuento)
        BigDecimal tasaIva = new BigDecimal("0.16");
        BigDecimal iva = subtotalConDescuento.multiply(tasaIva).setScale(2, RoundingMode.HALF_UP);

        // Retención IVA (4% sobre subtotal con descuento)
        BigDecimal tasaRetencionIva = new BigDecimal("0.04");
        BigDecimal retencionIva = subtotalConDescuento.multiply(tasaRetencionIva).setScale(2, RoundingMode.HALF_UP);

        // Total = subtotal - descuento + IVA - retención
        BigDecimal total = subtotalConDescuento.add(iva).subtract(retencionIva).setScale(2, RoundingMode.HALF_UP);

        // Asignar valores calculados
        pieza.setSubtotal(subtotal);
        pieza.setIva(iva);
        pieza.setRetencionIva(retencionIva);
        pieza.setTotalConImpuestos(total);
    }

    private BigDecimal calcularMontoDescuento(CotizacionPieza pieza) {
        if (pieza.getSubtotal() == null || pieza.getDescuento() == null) {
            return BigDecimal.ZERO;
        }
        return pieza.getSubtotal()
                .multiply(pieza.getDescuento())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    /**
     * RF-12.1: Validar que vehículo tenga marca, modelo y año
     */
    private void validarDatosVehiculo(Cotizacion cotizacion) {
        if (cotizacion.getMarca() == null || cotizacion.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca del vehículo es obligatoria");
        }
        if (cotizacion.getModelo() == null || cotizacion.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo del vehículo es obligatorio");
        }
        if (cotizacion.getAnio() == null || cotizacion.getAnio().trim().isEmpty()) {
            throw new IllegalArgumentException("El año del vehículo es obligatorio");
        }
    }
}

