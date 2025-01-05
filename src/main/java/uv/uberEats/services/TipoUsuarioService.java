package uv.uberEats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uv.uberEats.models.TipoUsuario;
import uv.uberEats.repositories.TipoUsuarioRepository;

import java.util.Optional;

@Service
public class TipoUsuarioService {
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuario getTipoUsuario(String nombre) {
        Optional<TipoUsuario> tipoUsuario = tipoUsuarioRepository.findByTipo(nombre);
        if (tipoUsuario.isPresent()) {
            return tipoUsuario.get();
        } else {
            throw new RuntimeException("Tipo de usuario no encontrado");
        }
    }
}
