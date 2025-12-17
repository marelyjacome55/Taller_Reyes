package com.tallerreyes.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuracion")
    private Long idConfiguracion;
    
    @Column(length = 100)
    private String clave;
    
    @Column(columnDefinition = "TEXT")
    private String valor;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}
