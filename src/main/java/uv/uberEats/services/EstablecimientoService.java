package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.repositories.EstablecimientoRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstablecimientoService {

    @Autowired
    EstablecimientoRepository establecimientoRepository;

    // Obtener todos los establecimientos ordenados por nombre Descendente ABC
    public List<Establecimiento> obtenerTodos() {
        return establecimientoRepository.findAll(Sort.by(Sort.Direction.DESC, "nombre"));
    }

    //Obtener todos por nombre buscado
    public List<Establecimiento> obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            // En caso de cadena vacía, devuelve todos
            return obtenerTodos();
        }
        return establecimientoRepository.findEstablecimientoByNombre("%" + nombre + "%");
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