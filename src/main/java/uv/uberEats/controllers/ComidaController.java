package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.models.Comida;
import uv.uberEats.services.ComidaService;
import uv.uberEats.services.EstablecimientoService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/comidas")
public class ComidaController {
    @Autowired
    private ComidaService comidaService;

    //Obtener todos u opcional buscando por nombre
    @GetMapping
    public List<Comida> getListaComidas(@RequestParam(required = false) String nombre) {
        if(nombre == null || nombre.isEmpty()) {
            return comidaService.obtenerTodasLasComida();
        }
        return comidaService.obtenerComidasPorNombre(nombre);
    }

    // Obtener comida por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getComidaPorId(@PathVariable Long id) {
        try {
            // Buscar comida por ID
            Comida comida = comidaService.obtenerComidaPorId(id);
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


}
