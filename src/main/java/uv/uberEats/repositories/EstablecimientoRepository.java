package uv.uberEats.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Establecimiento;

import java.util.List;

public interface EstablecimientoRepository extends JpaRepository <Establecimiento, Integer> {
    /*@Query("SELECT e FROM Establecimiento e WHERE e.nombre = :nombre")
    List<Establecimiento> findEstablecimientoByNombre(@Param("nombre") String nombre);

    List<Establecimiento> findByNombre(String nombre);*/
    @Query("Select e FROM Establecimiento e WHERE e.nombre like :nombre ORDER BY e.nombre")
    List<Establecimiento> findEstablecimientoByNombre(@Param("nombre") String nombre);
}
