package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.EstablecimientoComidasResponseDTO;
import uv.uberEats.dtos.EstablecimientoDTO;
import uv.uberEats.dtos.EstablecimientoResponseDTO;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.services.EstablecimientoService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/establecimientos")
public class EstablecimientoController {
    @Autowired
    EstablecimientoService establecimientoService;

    @GetMapping
    public ResponseEntity<List<EstablecimientoResponseDTO>> getListaEstablecimientos(@RequestParam(required = false) String nombre) {
        // Si no se proporciona un nombre, busca todos
        if (nombre == null || nombre.isEmpty()) {
            List<EstablecimientoResponseDTO> establecimientos = establecimientoService.obtenerTodos();
            return ResponseEntity.ok(establecimientos);
        }
        // Si se proporciona un nombre, realiza la búsqueda filtrada
        List<EstablecimientoResponseDTO> establecimientos = establecimientoService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(establecimientos);
    }


    // Buscar uno con ID este es el getDetails
    @GetMapping("/{id}")
    public ResponseEntity<?> getEstablecimientoPorId(@PathVariable Integer id) {
        try {
            // Obtener el DTO con establecimiento y sus comidas
            EstablecimientoComidasResponseDTO establecimientoComidasDTO = establecimientoService.obtenerEstablecimientoComidasPorId(id);
            return ResponseEntity.ok(establecimientoComidasDTO);
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

    //Agregar establecimiento
    @PostMapping("/agregar")
    public ResponseEntity<EstablecimientoResponseDTO> agregarEstablecimiento(@RequestBody EstablecimientoDTO establecimientoDTO) {
        try {
            // Llamada al servicio para agregar el establecimiento
            EstablecimientoResponseDTO nuevoEstablecimientoDTO = establecimientoService.agregarEstablecimiento(establecimientoDTO);

            // Retorna el DTO con el estado HTTP 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstablecimientoDTO);
        } catch (IllegalArgumentException e) {
            // En caso de error en la validación o parámetros incorrectos
            return ResponseEntity.badRequest().body(null); // Código 400 - Bad Request
        } catch (Exception e) {
            // En caso de un error interno
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Código 500 - Server Error
        }
    }


    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEstablecimiento(@PathVariable Integer id) {
        try {
            // Llama al servicio para eliminar el establecimiento
            Establecimiento eliminado = establecimientoService.eliminarEstablecimiento(id);
            return ResponseEntity.ok("Establecimiento eliminado: " + eliminado.getNombre());
        } catch (NoSuchElementException e) {
            // Si no existe el establecimiento, devuelve 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Establecimiento no encontrado con ID: " + id);
        } catch (Exception e) {
            // Manejo de errores generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarEstablecimiento(
            @PathVariable Integer id,
            @RequestBody EstablecimientoDTO establecimientoDTO) {
        try {
            // Llamar al servicio para actualizar el establecimiento
            EstablecimientoResponseDTO responseDTO = establecimientoService.actualizarEstablecimiento(id, establecimientoDTO);

            return ResponseEntity.ok(responseDTO); // Código 200 - OK
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Establecimiento no encontrado con ID: " + id); // Código 404 - Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Datos inválidos para actualizar el establecimiento."); // Código 400 - Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor."); // Código 500 - Server Error
        }
    }

}
