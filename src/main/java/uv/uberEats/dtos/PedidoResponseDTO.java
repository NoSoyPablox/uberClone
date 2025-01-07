package uv.uberEats.dtos;

public class PedidoResponseDTO {
    private Integer comidaId;
    private Integer cantidad;

    public PedidoResponseDTO(Integer comidaId, Integer cantidad) {
        this.comidaId = comidaId;
        this.cantidad = cantidad;
    }

    public PedidoResponseDTO()
    {

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
}
