package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.CarritoResponseDTO;
import uv.uberEats.dtos.ComidaResponseDTO;
import uv.uberEats.dtos.EstablecimientoDTO;
import uv.uberEats.dtos.PedidoResponseDTO;
import uv.uberEats.models.*;
import uv.uberEats.repositories.CarritoRepository;
import uv.uberEats.repositories.EstadoRepository;
import uv.uberEats.services.CarritoService;
import uv.uberEats.services.ComidaService;
import uv.uberEats.services.EstablecimientoService;
import uv.uberEats.services.UsuarioService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ComidaService comidaService;
    @Autowired
    private EstablecimientoService establecimientoService;
    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private EstadoRepository estadoRepository;


    //Obtener el carrito del usuario Activo
    @GetMapping("activo/{usuarioId}")
    public ResponseEntity<?> obtenerCarritoActivo(@PathVariable Integer usuarioId) {
        try {
            // Usar el servicio para obtener o crear el carrito activo
            Carrito carrito = carritoService.obtenerOCrearCarritoActivoPorUsuario(usuarioId);

            // Mapear la información a CarritoResponseDTO
            CarritoResponseDTO carritoDTO = new CarritoResponseDTO();
            carritoDTO.setId(carrito.getId());
            carritoDTO.setPrecioTotal(carrito.getPrecioTotal());
            carritoDTO.setEstadoNombre(carrito.getEstado().getNombre());
            carritoDTO.setUsuarioId(carrito.getUsuario().getId());
            carritoDTO.setRepartidorId(carrito.getIdRepartidor());

            // Mapear los pedidos asociados al carrito
            List<PedidoResponseDTO> pedidosDTO = carrito.getPedidos().stream().map(pedido -> {
                PedidoResponseDTO pedidoDTO = new PedidoResponseDTO();
                pedidoDTO.setPedidoId(pedido.getId());
                pedidoDTO.setCarritoId(carrito.getId());
                pedidoDTO.setComidaId(pedido.getComida().getId());
                pedidoDTO.setCantidad(pedido.getCantidad());
                pedidoDTO.setComidaNombre(pedido.getComida().getNombre());
                pedidoDTO.setComidaPrecio(pedido.getComida().getPrecio());
                pedidoDTO.setComidaImagen(pedido.getComida().getImagen());
                pedidoDTO.setLatitudEstablecimiento(pedido.getComida().getEstablecimiento().getLatitud());
                pedidoDTO.setLongitudEstablecimiento(pedido.getComida().getEstablecimiento().getLongitud());
                return pedidoDTO;
            }).collect(Collectors.toList());

            carritoDTO.setPedidos(pedidosDTO);

            // Devolver el DTO con código 200 OK
            return ResponseEntity.ok(carritoDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener o crear el carrito activo.");
        }
    }


    //Agregar un pedido al carrito
    @PostMapping("/agregar-pedido")
    public ResponseEntity<?> agregarPedido(
            @RequestParam Integer usuarioId,       // Recibe ID del usuario
            @RequestParam Integer comidaId,        // Recibe ID de la comida
            @RequestParam Integer cantidad         // Recibe cantidad
    ) {
        try {
            // Agregar el pedido al carrito usando el servicio
            Pedido pedidoAgregado = carritoService.agregarPedidoACarrito(usuarioId, comidaId, cantidad);

            ComidaResponseDTO comidaAgregada = comidaService.obtenerComidaPorId(comidaId);

            Establecimiento establecimiento = establecimientoService.obtenerPorId(comidaAgregada.getEstablecimientoId());

            // Mapear a PedidoResponseDTO
            PedidoResponseDTO pedidoDTO = new PedidoResponseDTO(
                    pedidoAgregado.getId(),
                    pedidoAgregado.getCarrito().getId(),
                    pedidoAgregado.getComida().getId(),
                    pedidoAgregado.getCantidad(),
                    comidaAgregada.getNombre(),
                    comidaAgregada.getPrecio(),
                    comidaAgregada.getImagen(),
                    establecimiento.getLatitud(),
                    establecimiento.getLongitud()
            );

            // Devolver el DTO como respuesta exitosa
            return ResponseEntity.ok(pedidoDTO);

        } catch (RuntimeException e) {
            // Manejar excepciones específicas
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Manejar errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }


    //Obtener los pedidos de un carrito
    @GetMapping("/{carritoId}/pedidos")
    public ResponseEntity<?> obtenerPedidosDelCarrito(@PathVariable Integer carritoId) {
        try {
            // Obtener los pedidos asociados al carrito
            Set<Pedido> pedidos = carritoService.obtenerPedidosDeCarrito(carritoId);
            return ResponseEntity.ok(pedidos);  // Devolver los pedidos encontrados
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Carrito no encontrado con ID: " + carritoId);
        }
    }

    @PutMapping("/{carritoId}/pendiente")
    public ResponseEntity<?> cambiarEstadoApendiente(@PathVariable Integer carritoId) {
        try {
            // Llamar al servicio para cambiar el estado
            CarritoResponseDTO carritoActualizado = carritoService.cambiarEstadoCarritoApendiente(carritoId);

            // Devolver el carrito actualizado como respuesta
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            // Manejar errores si no se puede cambiar el estado
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Manejar errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    //cambiar carrito a estado aceptado
    @PutMapping("/{carritoId}/aceptado")
    public ResponseEntity<?> cambiarEstadoAAceptado(@PathVariable Integer carritoId) {
        try {
            CarritoResponseDTO carritoActualizado = carritoService.cambiarEstadoCarritoAAceptado(carritoId);
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    //cambiar carrito a estado en transito
    @PutMapping("/{carritoId}/transito")
    public ResponseEntity<?> cambiarEstadoAEnTransito(@PathVariable Integer carritoId) {
        try {
            CarritoResponseDTO carritoActualizado = carritoService.cambiarEstadoCarritoAEnTransito(carritoId);
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    //cambiar carrito a estado aceptado
    @PutMapping("/{carritoId}/completado")
    public ResponseEntity<?> cambiarEstadoACompletado(@PathVariable Integer carritoId) {
        try {
            CarritoResponseDTO carritoActualizado = carritoService.cambiarEstadoCarritoACompletado(carritoId);
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    //cambiar carrito a estado cancelado
    @PutMapping("/{carritoId}/cancelado")
    public ResponseEntity<?> cambiarEstadoACancelado(@PathVariable Integer carritoId) {
        try {
            CarritoResponseDTO carritoActualizado = carritoService.cambiarEstadoCarritoACancelado(carritoId);
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    //Modificar cantidad en el carrito
    @PutMapping("/{carritoId}/modificar-pedido/{pedidoId}")
    public ResponseEntity<?> modificarCantidadPedido(
            @PathVariable Integer carritoId,   // ID del carrito
            @PathVariable Integer pedidoId,    // ID del pedido
            @RequestParam Integer nuevaCantidad // Nueva cantidad para el pedido
    ) {
        try {
            // Modificar el pedido en el carrito
            Carrito carritoActualizado = carritoService.modificarCantidadPedidoEnCarrito(carritoId, pedidoId, nuevaCantidad);

            // Buscar el pedido actualizado en el carrito
            Pedido pedidoActualizado = carritoActualizado.getPedidos().stream()
                    .filter(p -> p.getId().equals(pedidoId)) // Filtra por el ID del pedido
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado después de actualizarlo."));

            ComidaResponseDTO comidaAgregada = comidaService.obtenerComidaPorId(pedidoActualizado.getComida().getId());

            Establecimiento establecimiento = establecimientoService.obtenerPorId(comidaAgregada.getEstablecimientoId());

            // Mapear al DTO
            PedidoResponseDTO pedidoDTO = new PedidoResponseDTO(
                    pedidoActualizado.getId(),
                    carritoActualizado.getId(),
                    pedidoActualizado.getComida().getId(),
                    pedidoActualizado.getCantidad(),
                    comidaAgregada.getNombre(),
                    comidaAgregada.getPrecio(),
                    comidaAgregada.getImagen(),
                    establecimiento.getLatitud(),
                    establecimiento.getLongitud()
            );

            // Devolver el DTO como respuesta exitosa
            return ResponseEntity.ok(pedidoDTO);

        } catch (RuntimeException e) {
            // Manejar errores específicos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Manejar errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }


    // Eliminar un pedido del carrito
    @DeleteMapping("/{carritoId}/eliminar-pedido/{pedidoId}")
    public ResponseEntity<?> eliminarPedidoDelCarrito(
            @PathVariable Integer carritoId,
            @PathVariable Integer pedidoId
    ) {
        try {
            Carrito carritoActualizado = carritoService.eliminarPedidoDelCarrito(carritoId, pedidoId);
            return ResponseEntity.ok(carritoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<?> obtenerTodosLosCarritosPendientes() {
        return carritoService.obtenerCarritosPendientes();
    }

    //asdsad
    @GetMapping("/{usuarioId}/pendientes")
    public ResponseEntity<?> obtenerCarritosPendientesPorUsuario(@PathVariable Integer usuarioId) {
        return carritoService.obtenerCarritosPendientesPorUsuario(usuarioId);
    }

    // Endpoint para obtener carritos aceptados de un usuario
    @GetMapping("/{usuarioId}/aceptados")
    public ResponseEntity<?> obtenerCarritosAceptadosPorUsuario(@PathVariable Integer usuarioId) {
        return carritoService.obtenerCarritosAceptadosPorUsuario(usuarioId);
    }

    @GetMapping("/{usuarioId}/transito")
    public ResponseEntity<?> obtenerCarritosEnTransitoPorUsuario(@PathVariable Integer usuarioId) {
        return carritoService.obtenerCarritosEnTransitoPorUsuario(usuarioId);
    }

    @GetMapping("/{usuarioId}/completados")
    public ResponseEntity<?> obtenerCarritosCompletadosPorUsuario(@PathVariable Integer usuarioId) {
        return carritoService.obtenerCarritosCompletadosPorUsuario(usuarioId);
    }

    @GetMapping("/{usuarioId}/cancelados")
    public ResponseEntity<?> obtenerCarritosCanceladosPorUsuario(@PathVariable Integer usuarioId) {
        return carritoService.obtenerCarritosCanceladosPorUsuario(usuarioId);
    }

    @GetMapping("/repartidor/{repartidorId}/aceptados")
    public ResponseEntity<?> obtenerCarritosAceptados(@PathVariable Integer repartidorId) {
        return carritoService.obtenerCarritosAceptadosPorRepartidor(repartidorId);
    }

    // Endpoint para obtener carritos en tránsito de un repartidor
    @GetMapping("/repartidor/{repartidorId}/transito")
    public ResponseEntity<?> obtenerCarritosEnTransito(@PathVariable Integer repartidorId) {
        return carritoService.obtenerCarritosEnTransitoPorRepartidor(repartidorId);
    }

    @GetMapping("/repartidor/{repartidorId}/completados")
    public ResponseEntity<?> obtenerCarritosCompletados(@PathVariable Integer repartidorId) {
        return carritoService.obtenerCarritosCompletadosPorRepartidor(repartidorId);
    }
}
