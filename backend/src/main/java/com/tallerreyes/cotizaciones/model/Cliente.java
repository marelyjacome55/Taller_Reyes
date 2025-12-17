package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(nullable = false, length = 250)
    private String nombre;
    
    @Column(length = 150)
    private String correo;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 20)
    private String rfc;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false)
    private TipoCliente tipoCliente;
    
    @Column(nullable = false)
    private Boolean estatus = true;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
}
