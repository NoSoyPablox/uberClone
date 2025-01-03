package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Calificacion;
import uv.uberEats.repositories.CalificacionRepository;

import java.util.List;

@Service
public class CalificacionService {

    @Autowired
    CalificacionRepository calificacionRepository;

    //Obtener todas las calificaciones de una comida
    public List<Calificacion> obtenerCalificacionesDeComida(Long idComida)
    {
        return calificacionRepository.findByComidaId(idComida);
    }

    // Crear una calificación para una comida
    public Calificacion crearCalificacion(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }
}
