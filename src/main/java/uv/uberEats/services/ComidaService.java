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
    public Comida obtenerComidaPorId(Long id){
        return comidaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comida no encontrada con ID: " + id));

    }

}
