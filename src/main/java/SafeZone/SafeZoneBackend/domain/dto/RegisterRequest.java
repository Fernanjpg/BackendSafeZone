package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;

public class RegisterRequest {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
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
    public void setRoles(String rol) { this.roles = rol; }

    public RegionResumen getRegion() {
        return region;
    }
    public void setRegion(RegionResumen region) {
        this.region = region;
    }


}
