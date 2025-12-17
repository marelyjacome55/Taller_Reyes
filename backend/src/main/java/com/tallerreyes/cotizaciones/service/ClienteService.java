package com.tallerreyes.cotizaciones.service;

import com.tallerreyes.cotizaciones.model.Cliente;
import com.tallerreyes.cotizaciones.model.enums.TipoCliente;
import com.tallerreyes.cotizaciones.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * RF-04: Registrar cliente
     */
    public Cliente registrarCliente(Cliente cliente) {
        // Establecer fecha de ingreso si no viene
        if (cliente.getFechaIngreso() == null) {
            cliente.setFechaIngreso(LocalDate.now());
        }
        
        // Estatus activo por defecto
        if (cliente.getEstatus() == null) {
            cliente.setEstatus(true);
        }

        return clienteRepository.save(cliente);
    }

    /**
     * RF-05: Editar cliente
     */
    public Cliente actualizarCliente(Long idCliente, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // Actualizar campos
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setCorreo(clienteActualizado.getCorreo());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setRfc(clienteActualizado.getRfc());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setTipoCliente(clienteActualizado.getTipoCliente());
        cliente.setEstatus(clienteActualizado.getEstatus());

        return clienteRepository.save(cliente);
    }

    /**
     * Buscar cliente por ID
     */
    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    /**
     * RF-08: Visualizar clientes (todos)
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /**
     * RF-08: Filtrar por estatus
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEstatus(Boolean estatus) {
        return clienteRepository.findByEstatus(estatus);
    }

    /**
     * RF-08: Filtrar por tipo
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarPorTipo(TipoCliente tipoCliente) {
        return clienteRepository.findByTipoCliente(tipoCliente);
    }

    /**
     * RF-08: Filtrar por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Cliente> listarPorFechas(LocalDate inicio, LocalDate fin) {
        return clienteRepository.findByFechaIngresoBetween(inicio, fin);
    }

    /**
     * Buscar clientes por nombre (búsqueda flexible)
     */
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Activar/Desactivar cliente
     */
    public Cliente cambiarEstatus(Long idCliente, Boolean nuevoEstatus) {
        Cliente cliente = obtenerPorId(idCliente);
        cliente.setEstatus(nuevoEstatus);
        return clienteRepository.save(cliente);
    }
}
