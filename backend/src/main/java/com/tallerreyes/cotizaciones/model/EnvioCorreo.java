package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios_correo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioCorreo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envios_correo")
    private Long idEnviosCorreo;
    
    @ManyToOne
    @JoinColumn(name = "id_cotizaciones")
    private Cotizacion cotizacion;
    
    @Column(name = "correo_origen", length = 150)
    private String correoOrigen;
    
    @Column(name = "correo_destino", length = 150)
    private String correoDestino;
    
    @Column(length = 200)
    private String asunto;
    
    @Column(columnDefinition = "TEXT")
    private String cuerpo;
    
    @Column(name = "pdf_url", columnDefinition = "TEXT")
    private String pdfUrl;
    
    private Boolean exito;
    
    @Column(name = "mensaje_error", columnDefinition = "TEXT")
    private String mensajeError;
    
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    
    @Column(name = "respuesta_cliente", columnDefinition = "TEXT")
    private String respuestaCliente;
    
    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_resultante")
    private EstadoCotizacion estadoResultante;
}
