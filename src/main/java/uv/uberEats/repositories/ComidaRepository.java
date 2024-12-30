package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Comida;
import uv.uberEats.models.Establecimiento;

import java.util.List;

public interface ComidaRepository extends JpaRepository<Comida, Long> {
    @Query("SELECT c FROM Comida c WHERE c.nombre like :nombre ORDER BY c.nombre ")
    List<Comida> findComidasByNombre(@Param("nombre") String nombre);
}
