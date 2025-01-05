package uv.uberEats.models;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario", nullable = false)
    private Integer id;

    @Column(name = "tipo", nullable = false, length = 80)
    private String tipo;

    @OneToMany(mappedBy = "tipoUsuario")
    private Set<uv.uberEats.models.Usuario> usuarios = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Set<uv.uberEats.models.Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<uv.uberEats.models.Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}