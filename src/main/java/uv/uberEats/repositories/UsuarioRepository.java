package uv.uberEats.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uv.uberEats.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contrasenia = :contrasenia")
    Optional<Usuario> validateUsuario(@Param("correo") String correo, @Param("contrasenia") String contrasenia);
}
