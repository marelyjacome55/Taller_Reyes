package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.PuestoNombre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "puesto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Long idPuesto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PuestoNombre nombre;
    
    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;
}
