package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.CarritoResponseDTO;
import uv.uberEats.dtos.PedidoResponseDTO;
import uv.uberEats.models.Carrito;
import uv.uberEats.models.Pedido;
import uv.uberEats.services.CarritoService;

import java.util.*;

@RestController
@RequestMapping("api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("activo/{usuarioId}")
    public ResponseEntity<?> obtenerCarritoActivo(@PathVariable Integer usuarioId) {
        try {
            // Verificar el carrito antes de lanzar la excepción
            Optional<Carrito> carritoOpt = carritoService.obtenerCarritoActivoPorUsuario(usuarioId);
            if (carritoOpt.isEmpty()) {
                // Si el carrito no se encuentra, responder con 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró un carrito activo para el usuario con ID: " + usuarioId);
            }

            Carrito carrito = carritoOpt.get();
            // Mapear la información a CarritoConPedidosDTO
            CarritoResponseDTO carritoDTO = new CarritoResponseDTO();
            carritoDTO.setId(carrito.getId());
            carritoDTO.setPrecioTotal(carrito.getPrecioTotal());
            carritoDTO.setEstadoNombre(carrito.getEstado().getNombre());
            carritoDTO.setUsuarioId(carrito.getUsuario().getId());

            List<PedidoResponseDTO> pedidosDTO = new ArrayList<>();
            for (Pedido pedido : carrito.getPedidos()) {
                PedidoResponseDTO pedidoDTO = new PedidoResponseDTO();
                pedidoDTO.setComidaId(pedido.getComida().getId());
                pedidoDTO.setCantidad(pedido.getCantidad());
                pedidosDTO.add(pedidoDTO);
            }
            carritoDTO.setPedidos(pedidosDTO);

            return ResponseEntity.ok(carritoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el carrito activo.");
        }
    }


    //Agregar un pedido al carrito
    @PostMapping("/{carritoId}/agregar-pedido")
    public ResponseEntity<?> agregarPedido(
            @PathVariable Integer carritoId,
            @RequestParam Integer comidaId,       // Recibe ID de la comida
            @RequestParam Integer cantidad        // Recibe cantidad
    ) {
        try {
            Pedido pedidoAgregado = carritoService.agregarPedidoACarrito(carritoId, comidaId, cantidad);
            return ResponseEntity.ok(pedidoAgregado); // Devuelve el carrito actualizado
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
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

    //Cambiar estado a pendiente
    @PutMapping("/{carritoId}/pendiente")
    public ResponseEntity<?> cambiarEstadoApendiente(@PathVariable Integer carritoId) {
        try {
            // Llamar al servicio para cambiar el estado
            Carrito carritoActualizado = carritoService.cambiarEstadoCarritoApendiente(carritoId);
            return ResponseEntity.ok(carritoActualizado);  // Devolver el carrito actualizado
        } catch (RuntimeException e) {
            // Manejar errores si no se puede cambiar el estado
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
            // Llamar al servicio para modificar la cantidad
            Carrito carritoActualizado = carritoService.modificarCantidadPedidoEnCarrito(carritoId, pedidoId, nuevaCantidad);
            return ResponseEntity.ok(carritoActualizado); // Devolver el carrito actualizado
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
}
