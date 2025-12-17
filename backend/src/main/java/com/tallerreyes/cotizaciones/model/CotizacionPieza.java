package com.tallerreyes.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_piezas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionPieza {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion_piezas")
    private Long idCotizacionPiezas;
    
    @ManyToOne
    @JoinColumn(name = "id_cotizaciones")
    private Cotizacion cotizacion;
    
    @Column(length = 150)
    private String nombre;
    
    @Column(length = 50)
    private String unidad;
    
    private Integer cantidad;
    
    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal descuento;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal iva;
    
    @Column(name = "retencion_iva", precision = 12, scale = 2)
    private BigDecimal retencionIva;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "total_con_impuestos", precision = 12, scale = 2)
    private BigDecimal totalConImpuestos;
}
