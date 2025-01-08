package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.uberEats.models.Estado;
import uv.uberEats.services.EstadoService;

import java.util.Optional;

@RestController
@RequestMapping("/api/carritos")
public class EstadoController {
    //Solo uso interno
    @Autowired
    EstadoService estadoService;

    @GetMapping("estado/{nombre}")
    public ResponseEntity<?> obtenerEstadoPorNombre(@PathVariable String nombre) {
        Optional<Estado> estado = estadoService.obtenerEstadoPorNombre(nombre);

        if (estado.isPresent()) {
            return ResponseEntity.ok(estado.get()); // Si el estado existe, lo devuelve
        } else {
            return ResponseEntity.status(404).body("Estado no encontrado con nombre: " + nombre); // Si no, error 404
        }
    }
}
