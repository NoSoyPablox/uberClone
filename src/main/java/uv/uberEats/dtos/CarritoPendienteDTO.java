package uv.uberEats.dtos;

import java.math.BigDecimal;

public class CarritoPendienteDTO {
    private BigDecimal latitud;
    private BigDecimal longitud;

    // Getters y Setters
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
