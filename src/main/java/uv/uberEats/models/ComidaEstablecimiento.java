package uv.uberEats.models;

import jakarta.persistence.*;

@Entity
@Table(name = "comida_establecimiento")
public class ComidaEstablecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comida_establecimiento", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comida", nullable = false)
    private Comida comida;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "establecimiento", nullable = false)
    private uv.uberEats.models.Establecimiento establecimiento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Comida getComida() {
        return comida;
    }

    public void setComida(Comida comida) {
        this.comida = comida;
    }

    public uv.uberEats.models.Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(uv.uberEats.models.Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

}