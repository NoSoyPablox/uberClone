package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Calificacion;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    @Query("SELECT c FROM Calificacion c WHERE c.comida.id = :idComida")
    List<Calificacion> findByComidaId(@Param("idComida") Long idComida);

    // Método para contar las calificaciones de una comida
    @Query("SELECT COUNT(c) FROM Calificacion c WHERE c.comida.id = :idComida")
    Long countByComidaId(@Param("idComida") Integer idComida);

    // Método para obtener el promedio de calificaciones de una comida
    @Query("SELECT AVG(c.puntaje) FROM Calificacion c WHERE c.comida.id = :idComida")
    Double promedioDeCalificaciones(@Param("idComida") Integer idComida);
}
