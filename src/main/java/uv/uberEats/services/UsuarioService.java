package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.dtos.RegisterUsuarioDTO;
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

    public Usuario registrarUsuario(RegisterUsuarioDTO input){

        TipoUsuario tipoUsuario = tipoUsuarioService.getTipoUsuario("Cliente");

        Usuario usuario = new Usuario();
        usuario.setNombre(input.getNombre());
        usuario.setApellidoPaterno(input.getApellidoPaterno());
        usuario.setApellidoMaterno(input.getApellidoMaterno());
        usuario.setCorreo(input.getCorreo());
        usuario.setContrasenia(passwordEncoder.encode(input.getContrasenia()));
        usuario.setTipoUsuario(tipoUsuario);
        //Tengo que hacer que recupere el rol "Usuario" y ponerselo

        return usuarioRepository.save(usuario);
    }
}
