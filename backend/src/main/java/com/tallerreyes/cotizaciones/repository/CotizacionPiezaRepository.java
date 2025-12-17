package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.CotizacionPieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionPiezaRepository extends JpaRepository<CotizacionPieza, Long> {
    List<CotizacionPieza> findByCotizacion_IdCotizaciones(Long idCotizacion);
}
