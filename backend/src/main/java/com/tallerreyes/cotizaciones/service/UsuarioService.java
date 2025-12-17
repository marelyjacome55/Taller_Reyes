package com.tallerreyes.cotizaciones.service;

import com.tallerreyes.cotizaciones.model.Usuario;
import com.tallerreyes.cotizaciones.model.enums.EstatusUsuario;
import com.tallerreyes.cotizaciones.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * RF-01, RF-06: Registrar empleado
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar que el correo no exista
        usuarioRepository.findByCorreo(usuario.getCorreo())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("El correo ya está registrado");
                });
        
        return usuarioRepository.save(usuario);
    }

    /**
     * RF-07: Editar empleado
     */
    public Usuario actualizarUsuario(UUID idUsuario, Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Actualizar campos
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setPuesto(usuarioActualizado.getPuesto());
        usuario.setArea(usuarioActualizado.getArea());
        usuario.setEstatus(usuarioActualizado.getEstatus());

        return usuarioRepository.save(usuario);
    }

    /**
     * RF-24: Cambiar estatus (Activo/Inactivo)
     */
    public Usuario cambiarEstatus(UUID idUsuario, EstatusUsuario nuevoEstatus) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        usuario.setEstatus(nuevoEstatus);
        return usuarioRepository.save(usuario);
    }

    /**
     * Buscar usuario por ID
     */
    public Usuario obtenerPorId(UUID idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Buscar usuario por correo
     */
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Listar todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Listar usuarios por estatus
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarPorEstatus(EstatusUsuario estatus) {
        return usuarioRepository.findByEstatus(estatus);
    }

    /**
     * Listar usuarios por puesto
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarPorPuesto(String nombrePuesto) {
        return usuarioRepository.findByPuesto_Nombre(nombrePuesto);
    }
}
