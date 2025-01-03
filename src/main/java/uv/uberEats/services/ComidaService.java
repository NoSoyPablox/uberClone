package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Comida;
import uv.uberEats.repositories.ComidaRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ComidaService {

    @Autowired
    ComidaRepository comidaRepository;

    //Obtener todos los establecimientos por nombre descendente ABC
    public List<Comida> obtenerTodasLasComida(){
        return comidaRepository.findAll(Sort.by(Sort.Direction.DESC,"nombre"));
    }

    //Obtener todos por nombre buscado
    public List<Comida> obtenerComidasPorNombre(String nombre){
        if (nombre == null || nombre.isEmpty()){
            return obtenerTodasLasComida();
        }
        return comidaRepository.findComidasByNombre("%" + nombre + "%");
    }

    //Obtener comida por ID
    public Comida obtenerComidaPorId(Integer id){
        return comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));

    }

    //obtener comida por establecimiento
    public List<Comida> obtenerComidasPorEstablecimiento(Long idEstablecimiento) {
        return comidaRepository.findByEstablecimientoId(idEstablecimiento);
    }

    //Crear comida
    public Comida crearComida(Comida comida){
        return comidaRepository.save(comida);
    }

    // Eliminar comida por ID
    public void eliminarComida(Integer id) {
        Comida comida = comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));
        comidaRepository.delete(comida);
    }

    // Actualizar comida por ID
    public Comida actualizarComida(Integer id, Comida comidaActualizada) {
        Comida comidaExistente = comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));

        // Actualizar los atributos de la comida existente con los valores de la comida actualizada
        comidaExistente.setNombre(comidaActualizada.getNombre());
        comidaExistente.setPrecio(comidaActualizada.getPrecio());
        comidaExistente.setDescripcion(comidaActualizada.getDescripcion());
        comidaExistente.setImagen(comidaActualizada.getImagen());
        comidaExistente.setEstablecimiento(comidaActualizada.getEstablecimiento());

        // Guardar la comida actualizada
        return comidaRepository.save(comidaExistente);
    }
}
