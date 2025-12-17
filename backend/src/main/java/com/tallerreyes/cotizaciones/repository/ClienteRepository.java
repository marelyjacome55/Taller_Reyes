package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Cliente;
import com.tallerreyes.cotizaciones.model.enums.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEstatus(Boolean estatus);
    List<Cliente> findByTipoCliente(TipoCliente tipoCliente);
    List<Cliente> findByFechaIngresoBetween(LocalDate inicio, LocalDate fin);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
