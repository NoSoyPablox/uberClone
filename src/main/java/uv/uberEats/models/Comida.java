package uv.uberEats.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "comida")
public class Comida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comida", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "precio", nullable = false, precision = 5, scale = 2)
    private BigDecimal precio;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "establecimiento", nullable = false)
    @JsonBackReference
    private uv.uberEats.models.Establecimiento establecimiento;

    @OneToMany(mappedBy = "comida")
    @JsonManagedReference(value = "comida-calificacion")
    private Set<Calificacion> calificacions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "comida")
    @JsonManagedReference(value = "comida-pedido")
    private Set<uv.uberEats.models.Pedido> pedidos = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public uv.uberEats.models.Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(uv.uberEats.models.Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Set<Calificacion> getCalificacions() {
        return calificacions;
    }

    public void setCalificacions(Set<Calificacion> calificacions) {
        this.calificacions = calificacions;
    }

    public Set<uv.uberEats.models.Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<uv.uberEats.models.Pedido> pedidos) {
        this.pedidos = pedidos;
    }

}