package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Calificacion;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    @Query("SELECT c FROM Calificacion c WHERE c.comida.id = :idComida")
    List<Calificacion> findByComidaId(@Param("idComida") Long idComida);
}
