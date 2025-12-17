package com.tallerreyes.cotizaciones.model;

import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import com.tallerreyes.cotizaciones.model.enums.FormaPago;
import com.tallerreyes.cotizaciones.model.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cotizaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizaciones")
    private Long idCotizaciones;

    @Column(length = 20)
    private String folio;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago")
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(name = "condiciones_pago", length = 50)
    private String condicionesPago;

    @Column(length = 10)
    private String divisa;

    @Column(name = "tipo_cambio", precision = 10, scale = 4)
    private BigDecimal tipoCambio;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @Column(columnDefinition = "TEXT")
    private String encabezado;

    // VEHÍCULO (obligatorios: marca, modelo, año)
    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 10)
    private String anio;

    @Column(length = 100)
    private String version;

    @Column(length = 100)
    private String motor;

    // TOTALES
    @Column(precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "descuento_total", precision = 12, scale = 2)
    private BigDecimal descuentoTotal;

    @Column(name = "iva_total", precision = 12, scale = 2)
    private BigDecimal ivaTotal;

    @Column(name = "retencion_iva_total", precision = 12, scale = 2)
    private BigDecimal retencionIvaTotal;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "gran_total", precision = 12, scale = 2)
    private BigDecimal granTotal;

    @Enumerated(EnumType.STRING)
    private EstadoCotizacion estado;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Relaciones con tablas hijas (OneToMany)
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotizacionPieza> piezas = new ArrayList<>();

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotizacionServicio> servicios = new ArrayList<>();
}

