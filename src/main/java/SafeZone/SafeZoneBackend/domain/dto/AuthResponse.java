package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String roles; // Manteniendo el nombre exacto "roles" que definiste
    private RegionResumen region;

    // Constructor vacío obligatorio
    public AuthResponse() {}

    // Constructor con todos los campos para construirlo óptimamente en el Service
    public AuthResponse(String token, String nombre, String apellido, String email, String telefono, String roles, RegionResumen region) {
        this.token = token;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.roles = roles;
        this.region = region;
    }

    // Getters y Setters explícitos siguiendo la consistencia de tu código
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public RegionResumen getRegion() { return region; }
    public void setRegion(RegionResumen region) { this.region = region; }
}