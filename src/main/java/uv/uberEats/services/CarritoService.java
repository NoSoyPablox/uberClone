package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.models.*;
import uv.uberEats.repositories.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    EstadoRepository estadoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ComidaRepository comidaRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    //Obtener carrito activo del usuario
    public Optional<Carrito> obtenerCarritoActivoPorUsuario(Integer usuarioId) {
        return carritoRepository.findCarritoActivoByUsuarioId(usuarioId);
    }

    public Carrito crearCarrito(Integer usuarioId)
    {
        // Si no hay carrito activo, crear uno nuevo
        Carrito nuevoCarrito = new Carrito();

        // Aquí solo se necesita el id del usuario, se puede obtener el usuario desde la base de datos si es necesario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setPrecioTotal(BigDecimal.ZERO);
        BigDecimal coordenadas = new BigDecimal("00.0000");
        nuevoCarrito.setLatitud(coordenadas);
        nuevoCarrito.setLongitud(coordenadas);


        // Buscar estado 'Activo'
        Estado estadoActivo = estadoRepository.findByNombre("Activo")
                .orElseThrow(() -> new RuntimeException("Estado 'Activo' no encontrado"));

        nuevoCarrito.setEstado(estadoActivo);
        return carritoRepository.save(nuevoCarrito); // Guardar y devolver el nuevo carrito
    }

    //Agregar pedido a carrito dado un id de carrito (pedido tiene cantidad, carrito y comida asociada)
    public Pedido agregarPedidoACarrito(Integer carritoId, Integer comidaId, Integer cantidad){
        // Validar el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (!carrito.getEstado().getNombre().equals("Activo")) {
            throw new RuntimeException("El carrito no está activo");
        }

        // Validar la comida
        Comida comida = comidaRepository.findById(comidaId)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada"));

        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Crear nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setCarrito(carrito);
        pedido.setComida(comida);
        pedido.setCantidad(cantidad);

        // Guardar el pedido
        return pedidoRepository.save(pedido);
    }

    // Obtener los pedidos de un carrito
    public Set<Pedido> obtenerPedidosDeCarrito(Integer carritoId) {
        // Buscar el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Devolver los pedidos asociados al carrito
        return carrito.getPedidos();  // Devuelve los pedidos relacionados con el carrito
    }

    //Modificar el monto de una comida en el carrito
    public Carrito modificarCantidadPedidoEnCarrito(Integer carritoId, Integer pedidoId, Integer nuevaCantidad) {
        // Validar el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Verificar si el carrito está activo
        if (!carrito.getEstado().getNombre().equals("Activo")) {
            throw new RuntimeException("El carrito no está activo");
        }

        // Buscar el pedido en el carrito
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar si el pedido pertenece al carrito
        if (!pedido.getCarrito().getId().equals(carritoId)) {
            throw new RuntimeException("El pedido no pertenece al carrito especificado");
        }

        // Validar la nueva cantidad
        if (nuevaCantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        // Actualizar la cantidad en el pedido
        pedido.setCantidad(nuevaCantidad);
        pedidoRepository.save(pedido);

        return carritoRepository.save(carrito); // Guardar los cambios
    }

    //Eliminar producto del carrito
    public Carrito eliminarPedidoDelCarrito(Integer carritoId, Integer pedidoId) {
        // Buscar el carrito
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Verificar si el carrito está activo
        if (!carrito.getEstado().getNombre().equals("Activo")) {
            throw new RuntimeException("El carrito no está activo");
        }

        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar si el pedido pertenece al carrito
        if (!pedido.getCarrito().getId().equals(carritoId)) {
            throw new RuntimeException("El pedido no pertenece al carrito especificado");
        }

        // Eliminar el pedido
        pedidoRepository.delete(pedido);

        // Recalcular el precio total del carrito
        BigDecimal nuevoTotal = carrito.getPedidos().stream()
                .filter(p -> !p.getId().equals(pedidoId)) // Filtrar el pedido eliminado
                .map(p -> p.getComida().getPrecio().multiply(new BigDecimal(p.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrito.setPrecioTotal(nuevoTotal);

        // Guardar y devolver el carrito actualizado
        return carritoRepository.save(carrito);
    }

    //Cambiar estado a pendiente
    public Carrito cambiarEstadoCarritoApendiente(Integer carritoId) {
        // Obtener el carrito por su ID
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Verificar si el estado actual del carrito es "Activo"
        if (!carrito.getEstado().getNombre().equals("Activo")) {
            throw new RuntimeException("El carrito no está en estado 'Activo'");
        }
        // Buscar el estado "Pendiente" en la base de datos
        Estado estadoPendiente = estadoRepository.findByNombre("Pendiente")
                .orElseThrow(() -> new RuntimeException("Estado 'Pendiente' no encontrado"));

        // Cambiar el estado del carrito
        carrito.setEstado(estadoPendiente);

        // Guardar los cambios
        return carritoRepository.save(carrito);
    }
}
