package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.AreaNombre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "area")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Long idArea;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AreaNombre nombre;
}
