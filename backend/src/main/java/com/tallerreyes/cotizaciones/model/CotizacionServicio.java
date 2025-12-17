package com.tallerreyes.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_servicios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotizacionServicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion_servicios")
    private Long idCotizacionServicios;
    
    @ManyToOne
    @JoinColumn(name = "id_cotizaciones", nullable = false)
    private Cotizacion cotizacion;
    
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto = BigDecimal.ZERO;
}
