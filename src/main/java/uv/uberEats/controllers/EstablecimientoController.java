package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.models.Establecimiento;
import uv.uberEats.services.EstablecimientoService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/establecimientos")
public class EstablecimientoController {
    @Autowired
    EstablecimientoService establecimientoService;

    //Obtener todos
    // Obtener lista de establecimientos con búsqueda opcional por nombre
    @GetMapping
    public List<Establecimiento> getListaEstablecimientos(@RequestParam(required = false) String nombre) {
        // Si no se proporciona un nombre, busca todos
        if (nombre == null || nombre.isEmpty()) {
            return establecimientoService.obtenerTodos();
        }
        // Si se proporciona un nombre, realiza la búsqueda filtrada
        return establecimientoService.obtenerPorNombre(nombre);
    }


    // Buscar uno con ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEstablecimientoPorId(@PathVariable Long id) {
        try {
            // Buscar por ID
            Establecimiento establecimiento = establecimientoService.obtenerPorId(id);
            return ResponseEntity.ok(establecimiento);
        } catch (NoSuchElementException e) {
            // Manejar si no se encuentra
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
    public ResponseEntity<Establecimiento> agregarEstablecimiento(@RequestBody Establecimiento establecimiento) {
        try {
            Establecimiento nuevoEstablecimiento = establecimientoService.agregarEstablecimiento(establecimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstablecimiento); // Código 201 - Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Código 400 - Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Código 500 - Server Error
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEstablecimiento(@PathVariable Long id) {
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
            @PathVariable Long id,
            @RequestBody Establecimiento establecimiento) {
        try {
            // Verificar si el establecimiento existe
            Establecimiento existente = establecimientoService.obtenerPorId(id);

            // Actualizar datos
            existente.setNombre(establecimiento.getNombre());
            existente.setCodigoPostal(establecimiento.getCodigoPostal());
            existente.setNumero(establecimiento.getNumero());
            existente.setCalle(establecimiento.getCalle());
            existente.setPais(establecimiento.getPais());
            existente.setCiudad(establecimiento.getCiudad());
            existente.setEstado(establecimiento.getEstado());
            existente.setLatitud(establecimiento.getLatitud());
            existente.setLongitud(establecimiento.getLongitud());

            // Guardar cambios
            Establecimiento actualizado = establecimientoService.actualizarEstablecimiento(existente);

            return ResponseEntity.ok(actualizado); // Código 200 - OK
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
