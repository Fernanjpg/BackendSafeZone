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
            List<Regiones> regiones = regionesRepository.listarTodas();
            Regiones regionLima = regiones.stream()
                    .filter(r -> r.getNombreRegion().equals("Lima"))
                    .findFirst()
                    .orElse(new Regiones("15", "Lima"));

            RegionResumen regionResumen = new RegionResumen();
            regionResumen.setId(regionLima.getId());
            regionResumen.setNombre(regionLima.getNombreRegion());

            String[] emails = {"admin@example.com", "maria@example.com", "patricia@example.com", "carlos@example.com"};
            int creados = 0;

            for (String email : emails) {
                if (usuariosRepository.buscarUsuarioPorEmail(email) == null) {
                    Usuarios usuario = new Usuarios();
                    usuario.setId(UUID.randomUUID().toString());
                    usuario.setEmail(email);
                    usuario.setPassword(passwordEncoder.encode("password123"));
                    usuario.setTelefono("+34 000 000 000");
                    usuario.setEstado("ACTIVO");
                    usuario.setRegion(regionResumen);
                    usuario.setFecharegistro(Instant.now());

                    switch (email) {
                        case "admin@example.com" -> {
                            usuario.setNombre("Administrador");
                            usuario.setApellido("Sistema");
                            usuario.setRoles("ADMIN");
                            usuario.setTelefono("+34 123 456 789");
                        }
                        case "maria@example.com" -> {
                            usuario.setNombre("María");
                            usuario.setApellido("García");
                            usuario.setRoles("VICTIM");
                            usuario.setTelefono("+34 987 654 321");
                        }
                        case "patricia@example.com" -> {
                            usuario.setNombre("Patricia");
                            usuario.setApellido("López");
                            usuario.setRoles("PSYCHOLOGIST");
                            usuario.setTelefono("+34 555 666 777");
                        }
                        case "carlos@example.com" -> {
                            usuario.setNombre("Carlos");
                            usuario.setApellido("Rodríguez");
                            usuario.setRoles("DEFENDER");
                            usuario.setTelefono("+34 444 333 222");
                        }
                    }

                    usuariosRepository.guardar(usuario);
                    creados++;
                }
            }

            if (creados > 0) {
                System.out.println("✅ Se han creado " + creados + " usuarios de prueba en Cosmos DB.");
            } else {
                System.out.println("ℹ️ Los usuarios de prueba ya existen en la base de datos, saltando carga inicial.");
            }
        };
    }
}