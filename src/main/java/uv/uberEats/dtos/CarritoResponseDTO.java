package uv.uberEats.dtos;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponseDTO {
    private Integer id;
    private BigDecimal precioTotal;
    private String estadoNombre;  // Ahora tenemos el nombre del estado
    private Integer usuarioId; // Relación con el usuario (solo el ID)
    private BigDecimal latitud;
    private BigDecimal longitud;
    private List<PedidoResponseDTO> pedidos;

    public CarritoResponseDTO(Integer id, BigDecimal precioTotal, String estadoNombre, Integer usuarioId, BigDecimal latitud, BigDecimal longitud, List<PedidoResponseDTO> pedidos) {
        this.id = id;
        this.precioTotal = precioTotal;
        this.estadoNombre = estadoNombre;
        this.usuarioId = usuarioId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.pedidos = pedidos;
    }

    public CarritoResponseDTO()
    {

    }

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

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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

    public List<PedidoResponseDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<PedidoResponseDTO> pedidos) {
        this.pedidos = pedidos;
    }
}
