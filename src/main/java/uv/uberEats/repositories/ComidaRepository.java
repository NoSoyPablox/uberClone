package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Comida;

import java.util.List;

public interface ComidaRepository extends JpaRepository<Comida, Integer> {
    @Query("SELECT c FROM Comida c WHERE c.nombre like :nombre ORDER BY c.nombre ")
    List<Comida> findComidasByNombre(@Param("nombre") String nombre);

    @Query("SELECT c FROM Comida c WHERE c.establecimiento.id = :idEstablecimiento ORDER BY c.nombre")
    List<Comida> findByEstablecimientoId(@Param("idEstablecimiento") Long idEstablecimiento);
}
