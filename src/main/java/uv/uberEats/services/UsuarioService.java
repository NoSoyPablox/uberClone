package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.models.Usuario;
import uv.uberEats.models.UsuarioTipoUsuario;
import uv.uberEats.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    //Metodo para obtener el primer rol del usuario
    public String obtenerRolDelUsuario(Integer usuarioId){
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioTipoUsuario primerRol = usuario.getUsuarioTipoUsuarios().iterator().next();
        return primerRol.getTipoUsuario().getTipo(); // Retorna el nombre del tipo de usuario
    }
}
