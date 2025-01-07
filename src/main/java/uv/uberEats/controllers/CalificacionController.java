package uv.uberEats.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.CalificacionDTO;
import uv.uberEats.models.Calificacion;
import uv.uberEats.models.Comida;
import uv.uberEats.repositories.ComidaRepository;
import uv.uberEats.services.CalificacionService;
import uv.uberEats.services.ComidaService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;
    @Autowired
    private ComidaRepository comidaRepository;

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
    public ResponseEntity<?> crearCalificacion(@Valid @RequestBody CalificacionDTO calificacionDTO) {
        try {
            // Buscar la comida asociada
            Comida comida = comidaRepository.findById(calificacionDTO.getComidaId())
                    .orElseThrow(() -> new NoSuchElementException("Comida no encontrada."));

            // Crear la calificación
            Calificacion calificacion = new Calificacion();
            calificacion.setPuntaje(calificacionDTO.getPuntaje());
            calificacion.setComentario(calificacionDTO.getComentario());
            calificacion.setComida(comida);

            // Guardar la calificación
            Calificacion nuevaCalificacion = calificacionService.crearCalificacion(calificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCalificacion);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage()); // Código 404 - Not Found
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Datos inválidos para crear la calificación."); // Código 400 - Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor."); // Código 500 - Server Error
        }
    }
}
