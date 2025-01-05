package uv.uberEats.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "establecimiento")
public class Establecimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_establecimiento", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_postal", nullable = false, length = 5)
    private String codigoPostal;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "calle", nullable = false, length = 100)
    private String calle;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "estado", nullable = false, length = 100)
    private String estado;

    @Column(name = "latitud", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitud;

    @OneToMany(mappedBy = "establecimiento")
    @JsonManagedReference
    @JsonIgnore
    private Set<Comida> comidas = new LinkedHashSet<>();

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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Set<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(Set<Comida> comidas) {
        this.comidas = comidas;
    }

}