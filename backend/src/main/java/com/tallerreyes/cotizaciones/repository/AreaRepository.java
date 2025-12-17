package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Area;
import com.tallerreyes.cotizaciones.model.enums.AreaNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByNombre(AreaNombre nombre);
}
