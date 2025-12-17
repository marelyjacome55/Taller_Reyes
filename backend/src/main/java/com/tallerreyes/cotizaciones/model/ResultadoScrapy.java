package com.tallerreyes.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_scrapy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoScrapy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultados_scrapy")
    private Long idResultadosScrapy;
    
    @ManyToOne
    @JoinColumn(name = "id_cotizaciones")
    private Cotizacion cotizacion;
    
    @Column(name = "termino_busqueda", length = 150)
    private String terminoBusqueda;
    
    @Column(length = 50)
    private String fuente;
    
    @Column(length = 150)
    private String titulo;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal precio;
    
    @Column(length = 255)
    private String url;
    
    private LocalDateTime fecha;
}
