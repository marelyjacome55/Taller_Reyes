package com.tallerreyes.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "cache_scrapy")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheScrapy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cache_scrapy")
    private Long idCacheScrapy;
    
    @Column(length = 150)
    private String termino;
    
    @Column(name = "data_json", columnDefinition = "TEXT")
    private String dataJson;
    
    private LocalDateTime fecha;
}
