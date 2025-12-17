package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Cotizacion;
import com.tallerreyes.cotizaciones.model.enums.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    
    // RF-19: Filtrar cotizaciones por cliente
    List<Cotizacion> findByCliente_IdCliente(Long idCliente);
    
    // RF-25: Filtrar por estado
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
    
    // RF-25: Filtrar por usuario (empleado que creó)
    List<Cotizacion> findByUsuario_IdUsuario(UUID idUsuario);
    
    // RF-25: Filtrar por rango de fechas
    List<Cotizacion> findByFechaBetween(LocalDate inicio, LocalDate fin);
    
    // RF-19: Listar todas ordenadas por fecha descendente (más recientes primero)
    List<Cotizacion> findAllByOrderByFechaDesc();
}


