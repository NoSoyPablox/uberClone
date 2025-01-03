package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Estado;
import uv.uberEats.repositories.EstadoRepository;

import java.util.Optional;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public Optional<Estado> obtenerEstadoPorNombre(String nombre) {
        return estadoRepository.findByNombre(nombre);
    }

}
