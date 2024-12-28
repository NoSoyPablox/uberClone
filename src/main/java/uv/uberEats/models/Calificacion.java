package uv.uberEats.models;

import jakarta.persistence.*;

@Entity
@Table(name = "calificacion")
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_calificacion", nullable = false)
    private Integer id;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "puntaje", nullable = false)
    private Integer puntaje;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comida", nullable = false)
    private uv.uberEats.models.Comida comida;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public uv.uberEats.models.Comida getComida() {
        return comida;
    }

    public void setComida(uv.uberEats.models.Comida comida) {
        this.comida = comida;
    }

}