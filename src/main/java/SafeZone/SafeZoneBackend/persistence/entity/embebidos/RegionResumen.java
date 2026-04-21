package SafeZone.SafeZoneBackend.persistence.entity.embebidos;

public class RegionResumen {

    private String id;
    private String nombre;

    public RegionResumen() {}

    public RegionResumen(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



}
