package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.RegisterUsuarioDTO;
import uv.uberEats.dtos.RegisterUsuarioResponseDTO;
import uv.uberEats.models.Usuario;
import uv.uberEats.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para registrar un cliente
    @PostMapping("/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody RegisterUsuarioDTO registerUsuarioDTO) {
        try {
            RegisterUsuarioResponseDTO nuevoUsuario = usuarioService.registrarCliente(registerUsuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // Retornar el DTO creado con código 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage()); // Manejar errores y retornar mensaje
        }
    }

    // Endpoint para registrar un repartidor
    @PostMapping("/repartidor")
    public ResponseEntity<?> registrarRepartidor(@RequestBody RegisterUsuarioDTO registerUsuarioDTO) {
        try {
            RegisterUsuarioResponseDTO nuevoUsuario = usuarioService.registrarRepartidor(registerUsuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // Retornar el DTO creado con código 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage()); // Manejar errores y retornar mensaje
        }
    }

}
