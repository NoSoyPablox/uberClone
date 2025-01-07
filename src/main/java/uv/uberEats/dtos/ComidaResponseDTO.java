package uv.uberEats.dtos;

import java.math.BigDecimal;

public class ComidaResponseDTO {
    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private String descripcion;
    private byte[] imagen;
    private Double promedioCalificaciones;  // Campo para el promedio de calificaciones
    private Long conteoCalificaciones;

    public ComidaResponseDTO(Integer id, String nombre, BigDecimal precio, String descripcion, byte[] imagen , Double promedioCalificaciones, Long conteoCalificaciones) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.promedioCalificaciones = promedioCalificaciones;
        this.conteoCalificaciones = conteoCalificaciones;
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

    public Double getPromedioCalificaciones() {
        return promedioCalificaciones;
    }

    public void setPromedioCalificaciones(Double promedioCalificaciones) {
        this.promedioCalificaciones = promedioCalificaciones;
    }

    public Long getConteoCalificaciones() {
        return conteoCalificaciones;
    }

    public void setConteoCalificaciones(Long conteoCalificaciones) {
        this.conteoCalificaciones = conteoCalificaciones;
    }
}
