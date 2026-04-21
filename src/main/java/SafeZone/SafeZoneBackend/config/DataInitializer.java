package SafeZone.SafeZoneBackend.config;

import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
}