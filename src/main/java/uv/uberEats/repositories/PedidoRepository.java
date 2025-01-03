package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uv.uberEats.models.Pedido;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    // Buscar pedidos por usuario y estado (util para identificar el carrito activo del usuario)
    @Query("SELECT p FROM Pedido p WHERE p.carrito.usuario.id = :usuarioId AND p.carrito.estado.nombre = :estado")
    List<Pedido> findPedidosByUsuarioAndEstado(Long usuarioId, String estado);

    // Devolver los pedidos con su precio parcial calculado
    @Query("SELECT p FROM Pedido p JOIN FETCH p.comida c WHERE p.carrito.id = :carritoId")
    List<Pedido> findPedidosConPreciosParciales(Integer carritoId);
}
