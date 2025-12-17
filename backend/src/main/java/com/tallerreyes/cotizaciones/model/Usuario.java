package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.EstatusUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @Column(name = "id_usuario")
    private UUID idUsuario; // UUID de Supabase Auth
    
    @Column(nullable = false, length = 250)
    private String nombre;
    
    @Column(nullable = false, length = 150)
    private String correo;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;
    
    @ManyToOne
    @JoinColumn(name = "id_puesto")
    private Puesto puesto;
    
    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;
    
    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatusUsuario estatus;
}
