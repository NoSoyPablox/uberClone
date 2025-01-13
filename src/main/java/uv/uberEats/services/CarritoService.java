package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.CarritoResponseDTO;
import uv.uberEats.dtos.PedidoResponseDTO;
import uv.uberEats.models.*;
import uv.uberEats.repositories.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Carrito obtenerOCrearCarritoActivoPorUsuario(Integer usuarioId) {
        // Intentar obtener el carrito activo
        return carritoRepository.findCarritoActivoByUsuarioId(usuarioId)
                .orElseGet(() -> crearCarrito(usuarioId)); // Crear si no existe
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
    public Pedido agregarPedidoACarrito(Integer usuarioId, Integer comidaId, Integer cantidad) {
        // Obtener o crear el carrito activo del usuario
        Carrito carrito = obtenerOCrearCarritoActivoPorUsuario(usuarioId);

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
    public CarritoResponseDTO cambiarEstadoCarritoApendiente(Integer carritoId, BigDecimal latitud, BigDecimal longitud) {
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
        carrito.setEstado(estadoPendiente);

        // Actualizar latitud y longitud si se proporcionan
        if (latitud != null && longitud != null) {
            carrito.setLatitud(latitud);
            carrito.setLongitud(longitud);
        }

        //calcular el total del carrito:
        BigDecimal total = carrito.getPedidos().stream()
                .map(pedido -> {
                    BigDecimal precioComida = pedido.getComida().getPrecio();
                    Integer cantidad = pedido.getCantidad();
                    return precioComida.multiply(BigDecimal.valueOf(cantidad));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrito.setPrecioTotal(total);
        // Guardar los cambios
        carrito = carritoRepository.save(carrito);

        return mapearCarrito(carrito);
    }

    private CarritoResponseDTO cambiarEstadoCarrito(Integer carritoId, String nuevoEstado) {
        // Obtener el carrito por su ID
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Buscar el estado deseado en la base de datos
        Estado estado = estadoRepository.findByNombre(nuevoEstado)
                .orElseThrow(() -> new RuntimeException("Estado '" + nuevoEstado + "' no encontrado"));

        // Actualizar el estado del carrito
        carrito.setEstado(estado);

        // Guardar los cambios
        Carrito carritoActualizado = carritoRepository.save(carrito);

        // Mapear el carrito actualizado a DTO
        return mapearCarrito(carritoActualizado);
    }

    // Cambiar carrito a estado aceptado
    public CarritoResponseDTO cambiarEstadoCarritoAAceptado(Integer carritoId, Integer repartidorId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Cambiar estado a Aceptado
        Estado estado = estadoRepository.findByNombre("Aceptado")
                .orElseThrow(() -> new RuntimeException("Estado aceptado no encontrado"));
        carrito.setEstado(estado);

        // Asignar el repartidor
        carrito.setIdRepartidor(repartidorId);

        // Guardar el carrito actualizado
        carritoRepository.save(carrito);

        return mapearCarrito(carrito); // Asegúrate de que este DTO tiene los datos correctos.
    }

    // Cambiar carrito a estado en tránsito
    public CarritoResponseDTO cambiarEstadoCarritoAEnTransito(Integer carritoId) {
        return cambiarEstadoCarrito(carritoId, "En transito");
    }

    // Cambiar carrito a estado completado
    public CarritoResponseDTO cambiarEstadoCarritoACompletado(Integer carritoId) {
        return cambiarEstadoCarrito(carritoId, "Completado");
    }

    // Cambiar carrito a estado cancelado
    public CarritoResponseDTO cambiarEstadoCarritoACancelado(Integer carritoId) {
        return cambiarEstadoCarrito(carritoId, "Cancelado");
    }

    public ResponseEntity<?> obtenerCarritosPendientes() {
        try {
            // Obtener todos los carritos en estado "Pendiente"
            List<Carrito> carritosPendientes = carritoRepository.findCarritosPendientes();

            if (carritosPendientes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay carritos en estado 'Pendiente'.");
            }

            // Mapear los carritos a DTOs
            List<CarritoResponseDTO> responseDTOs = carritosPendientes.stream()
                    .map(carrito -> new CarritoResponseDTO(
                            carrito.getId(),
                            carrito.getPrecioTotal(),
                            carrito.getEstado().getNombre(),
                            carrito.getUsuario().getId(),
                            carrito.getLatitud(),
                            carrito.getLongitud(),
                            carrito.getIdRepartidor() != null ? carrito.getIdRepartidor() : null,
                            carrito.getPedidos().stream()
                                    .map(pedido -> new PedidoResponseDTO(
                                            pedido.getId(),
                                            carrito.getId(),
                                            pedido.getComida().getId(),
                                            pedido.getCantidad(),
                                            pedido.getComida().getNombre(),
                                            pedido.getComida().getPrecio(),
                                            pedido.getComida().getImagen(),
                                            pedido.getComida().getEstablecimiento().getLatitud(),
                                            pedido.getComida().getEstablecimiento().getLongitud()
                                    ))
                                    .toList()
                    ))
                    .toList();

            // Devolver la lista de DTOs con estado 200
            return ResponseEntity.ok(responseDTOs);

        } catch (Exception e) {
            // Manejar errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    public ResponseEntity<?> obtenerCarritosPendientesPorUsuario(Integer usuarioId) {
        List<Carrito> carritos = carritoRepository.findCarritosPendientesByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    // Obtener todos los carritos aceptados de un usuario
    public ResponseEntity<?> obtenerCarritosAceptadosPorUsuario(Integer usuarioId) {
        List<Carrito> carritos = carritoRepository.findCarritosAceptadosByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> obtenerCarritosEnTransitoPorUsuario(Integer usuarioId) {
        List<Carrito> carritos = carritoRepository.findCarritosEnTransitoByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> obtenerCarritosCompletadosPorUsuario(Integer usuarioId) {
        List<Carrito> carritos = carritoRepository.findCarritosCompletadosByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> obtenerCarritosCanceladosPorUsuario(Integer usuarioId) {
        List<Carrito> carritos = carritoRepository.findCarritosCanceladosByUsuarioId(usuarioId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    // Obtener todos los carritos aceptados de un repartidor
    public ResponseEntity<?> obtenerCarritosAceptadosPorRepartidor(Integer repartidorId) {
        List<Carrito> carritos = carritoRepository.findCarritosAceptadosByRepartidor(repartidorId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    // Obtener todos los carritos en tránsito de un repartidor
    public ResponseEntity<?> obtenerCarritosEnTransitoPorRepartidor(Integer repartidorId) {
        List<Carrito> carritos = carritoRepository.findCarritosEnTransitoByRepartidor(repartidorId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }

    // Obtener todos los carritos completados de un repartidor
    public ResponseEntity<?> obtenerCarritosCompletadosPorRepartidor(Integer repartidorId) {
        List<Carrito> carritos = carritoRepository.findCarritosCompletadosByRepartidor(repartidorId);
        if (carritos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<CarritoResponseDTO> response = mapearCarritos(carritos);
        return ResponseEntity.ok(response);
    }


    private List<CarritoResponseDTO> mapearCarritos(List<Carrito> carritos) {
        return carritos.stream().map(carrito -> new CarritoResponseDTO(
                carrito.getId(),
                carrito.getPrecioTotal(),
                carrito.getEstado().getNombre(),
                carrito.getUsuario().getId(),
                carrito.getLatitud(),
                carrito.getLongitud(),
                carrito.getIdRepartidor(),
                carrito.getPedidos().stream()
                        .map(pedido -> new PedidoResponseDTO(
                                pedido.getId(),
                                carrito.getId(),
                                pedido.getComida().getId(),
                                pedido.getCantidad(),
                                pedido.getComida().getNombre(),
                                pedido.getComida().getPrecio(),
                                pedido.getComida().getImagen(),
                                pedido.getComida().getEstablecimiento().getLatitud(),
                                pedido.getComida().getEstablecimiento().getLongitud()
                        ))
                        .toList()
        )).collect(Collectors.toList());
    }

    private CarritoResponseDTO mapearCarrito(Carrito carrito) {
        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getPrecioTotal(),
                carrito.getEstado().getNombre(),
                carrito.getUsuario().getId(),
                carrito.getLatitud(),
                carrito.getLongitud(),
                carrito.getIdRepartidor(),
                carrito.getPedidos().stream()
                        .map(pedido -> new PedidoResponseDTO(
                                pedido.getId(),
                                carrito.getId(),
                                pedido.getComida().getId(),
                                pedido.getCantidad(),
                                pedido.getComida().getNombre(),
                                pedido.getComida().getPrecio(),
                                pedido.getComida().getImagen(),
                                pedido.getComida().getEstablecimiento().getLatitud(),
                                pedido.getComida().getEstablecimiento().getLongitud()
                        ))
                        .toList()
        );
    }
}
