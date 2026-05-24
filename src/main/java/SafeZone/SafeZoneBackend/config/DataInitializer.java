package SafeZone.SafeZoneBackend.config;

import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadRegiones(RegionesRepository regionesRepository) {
        return args -> {
            // Verificamos si ya existen datos para no duplicarlos cada vez que inicies
            if (regionesRepository.listarTodas().isEmpty()) {

                List<Regiones> regionesPeru = List.of(
                        new Regiones("1", "Amazonas"),
                        new Regiones("2", "Ancash"),
                        new Regiones("3", "Apurímac"),
                        new Regiones("4", "Arequipa"),
                        new Regiones("5", "Ayacucho"),
                        new Regiones("6", "Cajamarca"),
                        new Regiones("7", "Callao"),
                        new Regiones("8", "Cusco"),
                        new Regiones("9", "Huancavelica"),
                        new Regiones("10", "Huánuco"),
                        new Regiones("11", "Ica"),
                        new Regiones("12", "Junín"),
                        new Regiones("13", "La Libertad"),
                        new Regiones("14", "Lambayeque"),
                        new Regiones("15", "Lima"),
                        new Regiones("16", "Loreto"),
                        new Regiones("17", "Madre de Dios"),
                        new Regiones("18", "Moquegua"),
                        new Regiones("19", "Pasco"),
                        new Regiones("20", "Piura"),
                        new Regiones("21", "Puno"),
                        new Regiones("22", "San Martín"),
                        new Regiones("23", "Tacna"),
                        new Regiones("24", "Tumbes"),
                        new Regiones("25", "Ucayali")
                );

                for (Regiones region : regionesPeru) {
                    regionesRepository.guardar(region);
                }

                System.out.println("✅ Se han cargado las 25 regiones (incluyendo Callao) en Cosmos DB.");
            } else {
                System.out.println("ℹ️ Las regiones ya existen en la base de datos, saltando carga inicial.");
            }
        };
    }

    @Bean
    CommandLineRunner loadUsuarios(UsuariosRepository usuariosRepository, RegionesRepository regionesRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Eliminar usuarios existentes de prueba para forzar recarga
            Usuarios adminExistente = usuariosRepository.buscarUsuarioPorEmail("admin@example.com");
            if (adminExistente != null) {
                usuariosRepository.eliminar(adminExistente);
            }
            Usuarios mariaExistente = usuariosRepository.buscarUsuarioPorEmail("maria@example.com");
            if (mariaExistente != null) {
                usuariosRepository.eliminar(mariaExistente);
            }
            Usuarios patriciaExistente = usuariosRepository.buscarUsuarioPorEmail("patricia@example.com");
            if (patriciaExistente != null) {
                usuariosRepository.eliminar(patriciaExistente);
            }
            Usuarios carlosExistente = usuariosRepository.buscarUsuarioPorEmail("carlos@example.com");
            if (carlosExistente != null) {
                usuariosRepository.eliminar(carlosExistente);
            }

            // Obtener la región Lima para los usuarios
            List<Regiones> regiones = regionesRepository.listarTodas();
            Regiones regionLima = regiones.stream()
                    .filter(r -> r.getNombreRegion().equals("Lima"))
                    .findFirst()
                    .orElse(new Regiones("15", "Lima"));

            RegionResumen regionResumen = new RegionResumen();
            regionResumen.setId(regionLima.getId());
            regionResumen.setNombre(regionLima.getNombreRegion());

            // Crear usuarios de prueba con contraseñas nuevas
            Usuarios admin = new Usuarios();
            admin.setId(java.util.UUID.randomUUID().toString());
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setTelefono("+34 123 456 789");
            admin.setRoles("ADMIN");
            admin.setEstado("ACTIVO");
            admin.setRegion(regionResumen);
            admin.setFecharegistro(java.time.Instant.now());
            usuariosRepository.guardar(admin);

            Usuarios maria = new Usuarios();
            maria.setId(java.util.UUID.randomUUID().toString());
            maria.setNombre("María");
            maria.setApellido("García");
            maria.setEmail("maria@example.com");
            maria.setPassword(passwordEncoder.encode("password123"));
            maria.setTelefono("+34 987 654 321");
            maria.setRoles("VICTIM");
            maria.setEstado("ACTIVO");
            maria.setRegion(regionResumen);
            maria.setFecharegistro(java.time.Instant.now());
            usuariosRepository.guardar(maria);

            Usuarios patricia = new Usuarios();
            patricia.setId(java.util.UUID.randomUUID().toString());
            patricia.setNombre("Patricia");
            patricia.setApellido("López");
            patricia.setEmail("patricia@example.com");
            patricia.setPassword(passwordEncoder.encode("password123"));
            patricia.setTelefono("+34 555 666 777");
            patricia.setRoles("PSYCHOLOGIST");
            patricia.setEstado("ACTIVO");
            patricia.setRegion(regionResumen);
            patricia.setFecharegistro(java.time.Instant.now());
            usuariosRepository.guardar(patricia);

            Usuarios carlos = new Usuarios();
            carlos.setId(java.util.UUID.randomUUID().toString());
            carlos.setNombre("Carlos");
            carlos.setApellido("Rodríguez");
            carlos.setEmail("carlos@example.com");
            carlos.setPassword(passwordEncoder.encode("password123"));
            carlos.setTelefono("+34 444 333 222");
            carlos.setRoles("DEFENDER");
            carlos.setEstado("ACTIVO");
            carlos.setRegion(regionResumen);
            carlos.setFecharegistro(java.time.Instant.now());
            usuariosRepository.guardar(carlos);

            System.out.println("✅ Se han cargado 4 usuarios de prueba en Cosmos DB.");
        };
    }
}