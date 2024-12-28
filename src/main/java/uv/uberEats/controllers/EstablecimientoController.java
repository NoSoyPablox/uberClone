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
@RequestMapping("api/Establecimientos")
public class EstablecimientoController {
    @Autowired
    EstablecimientoService establecimientoService;

    //Obtener todos
    @GetMapping
    public List<Establecimiento> getListaEstablecimientos() {
        List<Establecimiento> establecimientoList = establecimientoService.obtenerTodos();
        return establecimientoList;
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
}
