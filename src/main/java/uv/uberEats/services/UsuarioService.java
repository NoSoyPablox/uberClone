package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.RegisterUsuarioDTO;
import uv.uberEats.dtos.RegisterUsuarioResponseDTO;
import uv.uberEats.models.TipoUsuario;
import uv.uberEats.models.Usuario;
import uv.uberEats.repositories.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private TipoUsuarioService tipoUsuarioService;

    public UsuarioService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterUsuarioResponseDTO registrarCliente(RegisterUsuarioDTO input) {
        // Obtener el tipo de usuario "Cliente"
        TipoUsuario tipoUsuario = tipoUsuarioService.getTipoUsuario("Cliente");

        // Crear un nuevo usuario y asignar los datos
        Usuario usuario = new Usuario();
        usuario.setNombre(input.getNombre());
        usuario.setApellidoPaterno(input.getApellidoPaterno());
        usuario.setApellidoMaterno(input.getApellidoMaterno());
        usuario.setCorreo(input.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(input.getContrasenia()));
        usuario.setTipoUsuario(tipoUsuario);

        // Guardar el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Mapear y devolver el DTO
        return mapearUsuario(usuarioGuardado);
    }

    public RegisterUsuarioResponseDTO registrarRepartidor(RegisterUsuarioDTO input) {
        // Obtener el tipo de usuario "Repartidor"
        TipoUsuario tipoUsuario = tipoUsuarioService.getTipoUsuario("Repartidor");

        // Crear un nuevo usuario y asignar los datos
        Usuario usuario = new Usuario();
        usuario.setNombre(input.getNombre());
        usuario.setApellidoPaterno(input.getApellidoPaterno());
        usuario.setApellidoMaterno(input.getApellidoMaterno());
        usuario.setCorreo(input.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(input.getContrasenia()));
        usuario.setTipoUsuario(tipoUsuario);

        // Guardar el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Mapear y devolver el DTO
        return mapearUsuario(usuarioGuardado);
    }

    private RegisterUsuarioResponseDTO mapearUsuario(Usuario usuario) {
        return new RegisterUsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno(),
                usuario.getCorreo(),
                usuario.getTipoUsuario().getTipo()
        );
    }


}
