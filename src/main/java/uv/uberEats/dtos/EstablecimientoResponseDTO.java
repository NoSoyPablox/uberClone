package uv.uberEats.dtos;

import uv.uberEats.models.Establecimiento;

import java.math.BigDecimal;

public class EstablecimientoResponseDTO {
    private Integer id;
    private String nombre;
    private String codigoPostal;
    private Integer numero;
    private String calle;
    private String pais;
    private String ciudad;
    private String estado;
    private BigDecimal latitud;
    private BigDecimal longitud;

    public EstablecimientoResponseDTO(Integer id, String nombre, String codigoPostal, Integer numero, String calle, String pais, String ciudad, String estado, BigDecimal latitud, BigDecimal longitud) {
        this.id = id;
        this.nombre = nombre;
        this.codigoPostal = codigoPostal;
        this.numero = numero;
        this.calle = calle;
        this.pais = pais;
        this.ciudad = ciudad;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public EstablecimientoResponseDTO(Establecimiento establecimiento) {
        this.id = establecimiento.getId().intValue(); // Asumiendo que el ID es Long, lo convertimos a Integer
        this.nombre = establecimiento.getNombre();
        this.codigoPostal = establecimiento.getCodigoPostal();
        this.numero = establecimiento.getNumero();
        this.calle = establecimiento.getCalle();
        this.pais = establecimiento.getPais();
        this.ciudad = establecimiento.getCiudad();
        this.estado = establecimiento.getEstado();
        this.latitud = establecimiento.getLatitud();
        this.longitud = establecimiento.getLongitud();
    }

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
}
