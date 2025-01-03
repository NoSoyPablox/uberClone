package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.models.Calificacion;
import uv.uberEats.services.CalificacionService;

import java.util.List;

@RestController
@RequestMapping("api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @GetMapping("/{idComida}")
    public ResponseEntity<?> getCalificacionesDeComida(@PathVariable Long idComida) {
        try {
            // Obtener calificaciones
            List<Calificacion> calificaciones = calificacionService.obtenerCalificacionesDeComida(idComida);

            if (calificaciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay calificaciones registradas para la comida con ID: " + idComida);
            }

            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }

    //Crear calificacion para una comida
    @PostMapping
    public ResponseEntity<?> crearCalificacion(@RequestBody Calificacion calificacion) {
        try {
            // Crear nueva calificación
            Calificacion nuevaCalificacion = calificacionService.crearCalificacion(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCalificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Datos inválidos para crear la calificación."); // Código 400 - Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor."); // Código 500 - Server Error
        }
    }
}
