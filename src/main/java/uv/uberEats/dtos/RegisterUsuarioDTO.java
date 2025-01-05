package uv.uberEats.dtos;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterUsuarioDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    private String apellidoMaterno;

    @Email(message = "El correo electrónico debe ser válido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    public @NotBlank(message = "El nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El apellido paterno es obligatorio") String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(@NotBlank(message = "El apellido paterno es obligatorio") String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public @NotBlank(message = "El apellido materno es obligatorio") String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(@NotBlank(message = "El apellido materno es obligatorio") String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public @Email(message = "El correo electrónico debe ser válido") @NotBlank(message = "El correo es obligatorio") String getCorreo() {
        return correo;
    }

    public void setCorreo(@Email(message = "El correo electrónico debe ser válido") @NotBlank(message = "El correo es obligatorio") String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String getContrasenia() {
        return contrasenia;
    }

    public void setContraseña(@NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String contraseña) {
        this.contrasenia = contraseña;
    }
}
