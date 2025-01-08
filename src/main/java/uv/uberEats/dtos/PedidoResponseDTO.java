package uv.uberEats.dtos;

import java.math.BigDecimal;

public class PedidoResponseDTO {
    private Integer carritoId;
    private Integer comidaId;
    private Integer cantidad;
    private String comidaNombre;
    private BigDecimal comidaPrecio;
    private byte[] comidaImagen;
    private BigDecimal latitudEstablecimiento;
    private BigDecimal longitudEstablecimiento;

    public PedidoResponseDTO(Integer carritoId,Integer comidaId, Integer cantidad, String comidaNombre, BigDecimal comidaPrecio, byte[] comidaImagen, BigDecimal latitudEstablecimiento, BigDecimal longitudEstablecimiento) {
        this.carritoId = comidaId;
        this.comidaId = comidaId;
        this.cantidad = cantidad;
        this.comidaNombre = comidaNombre;
        this.comidaPrecio = comidaPrecio;
        this.comidaImagen = comidaImagen;
        this.latitudEstablecimiento = latitudEstablecimiento;
        this.longitudEstablecimiento = longitudEstablecimiento;
    }

    public PedidoResponseDTO()
    {

    }

    public Integer getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(Integer carritoId) {
        this.carritoId = carritoId;
    }

    public Integer getComidaId() {
        return comidaId;
    }

    public void setComidaId(Integer comidaId) {
        this.comidaId = comidaId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getComidaNombre() {
        return comidaNombre;
    }

    public void setComidaNombre(String comidaNombre) {
        this.comidaNombre = comidaNombre;
    }

    public BigDecimal getComidaPrecio() {
        return comidaPrecio;
    }

    public void setComidaPrecio(BigDecimal comidaPrecio) {
        this.comidaPrecio = comidaPrecio;
    }

    public byte[] getComidaImagen() {
        return comidaImagen;
    }

    public void setComidaImagen(byte[] comidaImagen) {
        this.comidaImagen = comidaImagen;
    }

    public BigDecimal getLatitudEstablecimiento() {
        return latitudEstablecimiento;
    }

    public void setLatitudEstablecimiento(BigDecimal latitudEstablecimiento) {
        this.latitudEstablecimiento = latitudEstablecimiento;
    }

    public BigDecimal getLongitudEstablecimiento() {
        return longitudEstablecimiento;
    }

    public void setLongitudEstablecimiento(BigDecimal longitudEstablecimiento) {
        this.longitudEstablecimiento = longitudEstablecimiento;
    }
}
