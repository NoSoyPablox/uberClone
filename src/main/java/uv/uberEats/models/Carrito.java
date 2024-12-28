package uv.uberEats.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito", nullable = false)
    private Integer id;

    @Column(name = "precio_total", nullable = false, precision = 5, scale = 2)
    private BigDecimal precioTotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado", nullable = false)
    private uv.uberEats.models.Estado estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario", nullable = false)
    private uv.uberEats.models.Usuario usuario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public uv.uberEats.models.Estado getEstado() {
        return estado;
    }

    public void setEstado(uv.uberEats.models.Estado estado) {
        this.estado = estado;
    }

    public uv.uberEats.models.Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(uv.uberEats.models.Usuario usuario) {
        this.usuario = usuario;
    }

}