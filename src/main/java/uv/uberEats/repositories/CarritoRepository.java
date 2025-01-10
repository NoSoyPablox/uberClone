package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uv.uberEats.models.Carrito;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    //Encontrar el carrito activo de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Activo'")
    Optional<Carrito> findCarritoActivoByUsuarioId(Integer usuarioId);

    //Encontrar los carritos pendientes de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Pendiente'")
    List<Carrito> findCarritosPendientesByUsuarioId(Integer usuarioId);

    //Encontrar los carritos aceptados de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Aceptado'")
    List<Carrito> findCarritosAceptadosByUsuarioId(Integer usuarioId);

    //Encontrar los carritos en transito de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'En transito'")
    List<Carrito> findCarritosEnTransitoByUsuarioId(Integer usuarioId);

    //Encontrar los carritos completados de un usuario
    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Completado'")
    List<Carrito> findCarritosCompletadosByUsuarioId(Integer usuarioId);

    @Query("SELECT c FROM Carrito c WHERE c.usuario.id = :usuarioId AND c.estado.nombre = 'Cancelado'")
    List<Carrito> findCarritosCanceladosByUsuarioId(Integer usuarioId);

    // Encontrar todos los carritos en estado "Pendiente" para que el repartidor pueda ver los carritos que se piden
    @Query("SELECT c FROM Carrito c WHERE c.estado.nombre = 'Pendiente'")
    List<Carrito> findCarritosPendientes();

    // Encontrar todos los carritos en estado "Aceptado" de un repartidor
    @Query("SELECT c FROM Carrito c WHERE c.idRepartidor = :repartidorId AND c.estado.nombre = 'Aceptado'")
    List<Carrito> findCarritosAceptadosByRepartidor(Integer repartidorId);

    @Query("SELECT c FROM Carrito c WHERE c.idRepartidor = :repartidorId AND c.estado.nombre = 'En transito'")
    List<Carrito> findCarritosEnTransitoByRepartidor(Integer repartidorId);

    //Encontrar todos los carritos en estado "Completado" de un repartidor
    @Query("SELECT c FROM Carrito c WHERE c.idRepartidor = :repartidorId AND c.estado.nombre = 'Completado'")
    List<Carrito> findCarritosCompletadosByRepartidor(Integer repartidorId);
}
