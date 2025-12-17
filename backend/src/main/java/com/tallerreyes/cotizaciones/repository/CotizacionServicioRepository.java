package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.CotizacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionServicioRepository extends JpaRepository<CotizacionServicio, Long> {
    List<CotizacionServicio> findByCotizacion_IdCotizaciones(Long idCotizacion);
}
