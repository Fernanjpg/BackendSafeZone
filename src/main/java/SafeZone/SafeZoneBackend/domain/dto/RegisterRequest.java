package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")

    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    @NotBlank(message = "El telefono no puede estar vacía")
    private String telefono;
    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "VICTIM|PSYCHOLOGIST|DEFENDER|ADMIN", message = "Rol no válido")
    private String roles;

    private RegionResumen region;

    // Constructor vacío (Obligatorio para que Spring lo entienda)
    public RegisterRequest() {}

    // Getters y Setters (Obligatorios para que el IDE no marque rojo)
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRoles() { return roles; }
    public void setRoles(String rol) {
        this.roles = rol == null ? null : rol.trim().toUpperCase();
    }

    public RegionResumen getRegion() {
        return region;
    }
    public void setRegion(RegionResumen region) {
        this.region = region;
    }


}
