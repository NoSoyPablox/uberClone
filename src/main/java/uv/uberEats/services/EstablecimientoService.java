package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.EstablecimientoResponseDTO;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.repositories.EstablecimientoRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class EstablecimientoService {

    @Autowired
    EstablecimientoRepository establecimientoRepository;

    public List<EstablecimientoResponseDTO> obtenerTodos() {
        List<Establecimiento> establecimientos = establecimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
        return establecimientos.stream()
                .map(establecimiento -> new EstablecimientoResponseDTO(establecimiento))
                .collect(Collectors.toList());
    }

    //Obtener todos por nombre buscado
    public List<EstablecimientoResponseDTO> obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return obtenerTodos();
        }
        List<Establecimiento> establecimientos = establecimientoRepository.findEstablecimientoByNombre("%" + nombre + "%");
        return establecimientos.stream()
                .map(establecimiento -> new EstablecimientoResponseDTO(establecimiento))
                .collect(Collectors.toList());
    }

    // Obtener un establecimiento por ID
    public Establecimiento obtenerPorId(Long id) {
        return establecimientoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Establecimiento no encontrado con ID: " + id));
    }

    //Agregar un establecimiento
    public Establecimiento agregarEstablecimiento(Establecimiento establecimiento) {
        return establecimientoRepository.save(establecimiento);
    }

    //Eliminar un establecimiento
    public Establecimiento eliminarEstablecimiento(Long id) {
        Establecimiento establecimiento = obtenerPorId(id);
        establecimientoRepository.delete(establecimiento);
        return establecimiento;
    }

    //Modificar un establecimiento
    public Establecimiento actualizarEstablecimiento(Establecimiento establecimiento) {
        return establecimientoRepository.save(establecimiento);
    }
}