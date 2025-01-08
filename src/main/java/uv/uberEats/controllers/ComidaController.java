package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uv.uberEats.dtos.ComidaResponseDTO;
import uv.uberEats.models.Comida;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.services.ComidaService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/comidas")
public class ComidaController {
    @Autowired
    private ComidaService comidaService;

    // Obtener todos u opcional buscando por nombre
    @GetMapping
    public List<ComidaResponseDTO> getListaComidas(@RequestParam(required = false) String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return comidaService.obtenerTodasLasComida();
        }
        return comidaService.obtenerComidasPorNombre(nombre);
    }

    // Obtener comida por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getComidaPorId(@PathVariable Integer id) {
        try {
            // Buscar comida por ID
            ComidaResponseDTO comida = comidaService.obtenerComidaPorId(id);
            return ResponseEntity.ok(comida);
        } catch (NoSuchElementException e) {
            // Manejar si no se encuentra
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comida no encontrada con ID: " + id);
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    // Obtener comida por establecimiento dado su Id
    @GetMapping("/establecimiento/{id}")
    public ResponseEntity<?> getComidasPorEstablecimiento(@PathVariable Integer id) {
        try {
            // Buscar comidas por ID del establecimiento
            List<ComidaResponseDTO> comidas = comidaService.obtenerComidasPorEstablecimiento(id);

            // Validar si no se encontraron comidas
            if (comidas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay comidas registradas para el establecimiento con ID: " + id);
            }

            return ResponseEntity.ok(comidas);
        } catch (NoSuchElementException e) {
            // Manejar si no se encuentra el establecimiento
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Establecimiento no encontrado con ID: " + id);
        } catch (Exception e) {
            // Manejar otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PostMapping
    public ResponseEntity<?> agregarComida(@RequestParam("nombre") String nombre,
                                           @RequestParam("precio") String precio,
                                           @RequestParam("descripcion") String descripcion,
                                           @RequestParam("establecimientoId") Integer establecimientoId,
                                           @RequestParam("imagen") MultipartFile imagen) {
        try {
            // Convertir la imagen a un arreglo de bytes
            byte[] imagenBytes = imagen.getBytes();

            // Llamar al servicio para crear la comida y obtener el DTO de respuesta
            ComidaResponseDTO comidaResponse = comidaService.crearComida(nombre, precio, descripcion, establecimientoId, imagenBytes);

            return ResponseEntity.status(HttpStatus.CREATED).body(comidaResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al procesar la imagen.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarComida(@PathVariable Integer id) {
        try {
            comidaService.eliminarComida(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Comida eliminada con éxito."); // Código 204 - No Content
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comida no encontrada con ID: " + id); // Código 404 - Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor."); // Código 500 - Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarComida(@PathVariable Integer id,
                                              @RequestParam(value = "nombre", required = false) String nombre,
                                              @RequestParam(value = "precio", required = false) String precio,
                                              @RequestParam(value = "descripcion", required = false) String descripcion,
                                              @RequestParam(value = "establecimientoId", required = false) Integer establecimientoId,
                                              @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        try {
            // Convertir la imagen a un arreglo de bytes si se proporciona
            byte[] imagenBytes = null;
            if (imagen != null) {
                imagenBytes = imagen.getBytes();
            }

            // Llamar al servicio para actualizar la comida y pasar la imagen si existe
            ComidaResponseDTO comidaResponse = comidaService.actualizarComida(id, nombre, precio, descripcion, establecimientoId, imagenBytes);

            return ResponseEntity.ok(comidaResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al procesar la imagen.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Comida no encontrada con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }


}
