package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.ResultadoScrapy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoScrapyRepository extends JpaRepository<ResultadoScrapy, Long> {
    // RF-22: Buscar resultados asociados a una cotización
    List<ResultadoScrapy> findByCotizacion_IdCotizaciones(Long idCotizacion);
    List<ResultadoScrapy> findByTerminoBusqueda(String termino);
}
