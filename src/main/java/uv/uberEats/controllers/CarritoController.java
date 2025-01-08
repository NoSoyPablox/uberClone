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
import uv.uberEats.services.CarritoService;
import uv.uberEats.services.ComidaService;
import uv.uberEats.services.EstablecimientoService;
import uv.uberEats.services.UsuarioService;

import java.util.*;

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


    //Obtener el carrito del usuario Activo
    @GetMapping("activo/{usuarioId}")
    public ResponseEntity<?> obtenerCarritoActivo(@PathVariable Integer usuarioId) {
        try {
            // Verificar si existe un carrito activo para el usuario
            Optional<Carrito> carritoOpt = carritoService.obtenerCarritoActivoPorUsuario(usuarioId);

            Carrito carrito;
            boolean creado = false; // Bandera para verificar si se creó un nuevo carrito

            if (carritoOpt.isEmpty()) {
                // Si no existe, intentar crearlo
                carrito = carritoService.crearCarrito(usuarioId);
                creado = true; // Marcar que se creó un carrito
            } else {
                // Si ya existe, obtenerlo
                carrito = carritoOpt.get();
            }

            // Mapear la información a CarritoResponseDTO
            CarritoResponseDTO carritoDTO = new CarritoResponseDTO();
            carritoDTO.setId(carrito.getId());
            carritoDTO.setPrecioTotal(carrito.getPrecioTotal());
            carritoDTO.setEstadoNombre(carrito.getEstado().getNombre());
            carritoDTO.setUsuarioId(carrito.getUsuario().getId());

            // Mapear los pedidos asociados al carrito
            List<PedidoResponseDTO> pedidosDTO = new ArrayList<>();
            for (Pedido pedido : carrito.getPedidos()) {
                PedidoResponseDTO pedidoDTO = new PedidoResponseDTO();
                pedidoDTO.setCarritoId(carrito.getId());
                pedidoDTO.setComidaId(pedido.getComida().getId());
                pedidoDTO.setCantidad(pedido.getCantidad());
                pedidoDTO.setComidaNombre(pedido.getComida().getNombre());
                pedidoDTO.setComidaPrecio(pedido.getComida().getPrecio());
                pedidoDTO.setComidaImagen(pedido.getComida().getImagen());
                pedidoDTO.setLatitudEstablecimiento(pedido.getComida().getEstablecimiento().getLatitud());
                pedidoDTO.setLongitudEstablecimiento(pedido.getComida().getEstablecimiento().getLongitud());
                pedidosDTO.add(pedidoDTO);
            }
            carritoDTO.setPedidos(pedidosDTO);

            // Si se creó un nuevo carrito, devolver 201 Created
            if (creado) {
                return ResponseEntity.status(HttpStatus.CREATED).body(carritoDTO);
            }

            // Si ya existía, devolver 200 OK
            return ResponseEntity.ok(carritoDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener o crear el carrito activo.");
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
            // Agregar el pedido al carrito usando el servicio
            Pedido pedidoAgregado = carritoService.agregarPedidoACarrito(carritoId, comidaId, cantidad);

            ComidaResponseDTO comidaAgregada = comidaService.obtenerComidaPorId(comidaId);

            Establecimiento establecimiento = establecimientoService.obtenerPorId(comidaAgregada.getEstablecimientoId());

            // Mapear a PedidoResponseDTO
            PedidoResponseDTO pedidoDTO = new PedidoResponseDTO(
                    carritoId,
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
}
