package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uv.uberEats.models.Estado;

import java.util.List;
import java.util.Optional;

public interface EstadoRepository extends JpaRepository <Estado, Integer> {
    //Obtener el estado que se buscó
    Optional<Estado> findByNombre(String nombre);
}
