package uv.uberEats.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Column(name = "latitud", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;

    @OneToMany(mappedBy = "carrito")
    @JsonManagedReference
    private Set<uv.uberEats.models.Pedido> pedidos = new LinkedHashSet<>();

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

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public Set<uv.uberEats.models.Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<uv.uberEats.models.Pedido> pedidos) {
        this.pedidos = pedidos;
    }

}