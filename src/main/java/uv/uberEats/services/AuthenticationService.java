package uv.uberEats.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.LoginUsuarioDTO;
import uv.uberEats.models.Usuario;
import uv.uberEats.repositories.UsuarioRepository;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Usuario authenticate(LoginUsuarioDTO input){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getCorreo(), input.getContrasenia())
            );

            //Si la autenticacion es exitosa buscamos el usuario en la base de datos
            Optional<Usuario> usuario = usuarioRepository.findByCorreo(input.getCorreo());
            return usuario.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        } catch (Exception e) {
            // Manejo de errores: Si ocurre una excepción en la autenticación, lanzamos una excepción
            throw new IllegalArgumentException("Credenciales incorrectas");
        }
    }
}
