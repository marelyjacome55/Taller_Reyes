package com.tallerreyes.cotizaciones.repository;

import com.tallerreyes.cotizaciones.model.Usuario;
import com.tallerreyes.cotizaciones.model.enums.EstatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByEstatus(EstatusUsuario estatus);
    List<Usuario> findByPuesto_Nombre(String puestoNombre);
}
