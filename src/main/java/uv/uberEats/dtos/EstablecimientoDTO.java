package uv.uberEats.dtos;

import java.math.BigDecimal;

public class EstablecimientoDTO {
    private String nombre;
    private String codigoPostal;
    private Integer numero;
    private String calle;
    private String pais;
    private String ciudad;
    private String estado;
    private BigDecimal latitud;
    private BigDecimal longitud;

    public EstablecimientoDTO(String nombre, String codigoPostal, Integer numero, String calle, String pais, String ciudad, String estado, BigDecimal latitud, BigDecimal longitud) {
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
