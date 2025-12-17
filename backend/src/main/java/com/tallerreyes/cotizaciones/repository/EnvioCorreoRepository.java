package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.EnvioCorreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioCorreoRepository extends JpaRepository<EnvioCorreo, Long> {
    List<EnvioCorreo> findByCotizacion_IdCotizaciones(Long idCotizacion);
    List<EnvioCorreo> findByExito(Boolean exito);
}
