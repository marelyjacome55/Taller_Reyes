package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.CacheScrapy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CacheScrapyRepository extends JpaRepository<CacheScrapy, Long> {
    // RF-21: Buscar caché por término
    Optional<CacheScrapy> findByTerminoAndFechaAfter(String termino, LocalDateTime fechaMinima);
}
