package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uv.uberEats.models.TipoUsuario;

import java.util.Optional;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Integer> {
    Optional<TipoUsuario> findByTipo(String nombre);
}
