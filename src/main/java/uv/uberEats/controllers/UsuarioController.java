package uv.uberEats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uv.uberEats.dtos.RegisterUsuarioDTO;
import uv.uberEats.models.Usuario;
import uv.uberEats.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegisterUsuarioDTO registerUsuarioDTO) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(registerUsuarioDTO);
            return ResponseEntity.status(201).body(nuevoUsuario); // Retornar el usuario creado con código 201 (creado)
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null); // Si hay un error, retornar código 400
        }
    }

}
