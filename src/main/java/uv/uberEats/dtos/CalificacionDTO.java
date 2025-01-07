package uv.uberEats.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CalificacionDTO {
    @NotNull(message = "El puntaje no puede ser nulo.")
    @Min(value = 1, message = "El puntaje debe ser al menos 1.")
    @Max(value = 5, message = "El puntaje no puede ser mayor que 5.")
    private Integer puntaje;

    private String comentario;

    @NotNull(message = "El ID de la comida no puede ser nulo.")
    private Integer comidaId;

    public @NotNull(message = "El puntaje no puede ser nulo.") @Min(value = 1, message = "El puntaje debe ser al menos 1.") @Max(value = 5, message = "El puntaje no puede ser mayor que 5.") Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(@NotNull(message = "El puntaje no puede ser nulo.") @Min(value = 1, message = "El puntaje debe ser al menos 1.") @Max(value = 5, message = "El puntaje no puede ser mayor que 5.") Integer puntaje) {
        this.puntaje = puntaje;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public @NotNull(message = "El ID de la comida no puede ser nulo.") Integer getComidaId() {
        return comidaId;
    }

    public void setComidaId(@NotNull(message = "El ID de la comida no puede ser nulo.") Integer comidaId) {
        this.comidaId = comidaId;
    }

    public CalificacionDTO(Integer puntaje, String comentario, Integer comidaId) {
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.comidaId = comidaId;
    }
}
