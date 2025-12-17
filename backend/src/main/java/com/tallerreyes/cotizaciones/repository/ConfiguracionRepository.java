package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    // RF-23: Buscar configuración por clave
    Optional<Configuracion> findByClave(String clave);
}
