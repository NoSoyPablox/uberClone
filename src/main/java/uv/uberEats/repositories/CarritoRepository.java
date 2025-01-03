package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uv.uberEats.models.Carrito;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    //Encontrar el carrito activo de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Activo'")
    Optional<Carrito> findCarritoActivoByUsuarioId(Integer usuarioId);
}
