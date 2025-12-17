package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Puesto;
import com.tallerreyes.cotizaciones.model.enums.PuestoNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long> {
    Optional<Puesto> findByNombre(PuestoNombre nombre);
}
