package uv.uberEats.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uv.uberEats.dtos.LoginResponseDTO;
import uv.uberEats.dtos.LoginUsuarioDTO;
import uv.uberEats.models.Usuario;
import uv.uberEats.services.AuthenticationService;
import uv.uberEats.services.JwtService;

@RequestMapping("api/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUsuarioDTO loginUsuarioDTO) {
        Usuario authenticatedUser = authenticationService.authenticate(loginUsuarioDTO);
        if (authenticatedUser != null) {
            // Generar el JWT
            String jwtToken = jwtService.generateToken(authenticatedUser);

            // Crear la respuesta con el token y su tiempo de expiración
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(jwtService.getJwtExpiration());

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
