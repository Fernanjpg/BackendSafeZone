package SafeZone.SafeZoneBackend.modules.alerta;

import SafeZone.SafeZoneBackend.domain.Repository.AlertaEmergenciaRepository;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaResponse;
import SafeZone.SafeZoneBackend.domain.dto.AtenderAlertaRequest;
import SafeZone.SafeZoneBackend.domain.service.AlertaEmergenciaService;
import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests de unidad para AlertaEmergenciaService.
 * <p>
 * RF-03: verifican que la lógica de fuente de ubicación (GPS / MANUAL / GPS_Y_MANUAL)
 * y la validación de "al menos una ubicación" funcionen correctamente.
 * No requieren Cosmos DB ni conexión a internet.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RF-03 — AlertaEmergenciaService")
class AlertaEmergenciaServiceTest {

    @Mock
    private AlertaEmergenciaRepository alertaRepository;

    @InjectMocks
    private AlertaEmergenciaService alertaService;

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Construye una entidad guardada con los datos mínimos para las respuestas. */
    private AlertaEmergencia entidadGuardada(String fuenteUbicacion,
                                             Double lat, Double lon,
                                             String direccionManual) {
        return AlertaEmergencia.builder()
                .id("test-id-123")
                .victimaId("v-001")
                .victimaNombre("Ana López")
                .victimaEmail("ana@test.com")
                .latitud(lat)
                .longitud(lon)
                .precision(lat != null ? 10.0 : null)
                .direccionManual(direccionManual)
                .fuenteUbicacion(fuenteUbicacion)
                .mensaje("Necesito ayuda")
                .estado("ACTIVA")
                .creadoEn(Instant.now())
                .build();
    }

    // ════════════════════════════════════════════════════════════════════════
    // RF-03 — Fuente de ubicación
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Fuente de ubicación")
    class FuenteUbicacion {

        @Test
        @DisplayName("Solo GPS → fuenteUbicacion debe ser 'GPS'")
        void soloGps_deberia_asignarFuenteGps() {
            // Arrange
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana López");
            request.setLatitud(-12.0464);
            request.setLongitud(-77.0428);
            request.setPrecision(15.0);

            when(alertaRepository.guardar(any())).thenAnswer(inv -> {
                AlertaEmergencia a = inv.getArgument(0);
                return entidadGuardada(a.getFuenteUbicacion(),
                        a.getLatitud(), a.getLongitud(), a.getDireccionManual());
            });

            // Act
            AlertaEmergenciaResponse response = alertaService.crearAlerta(request);

            // Assert
            assertThat(response.getFuenteUbicacion()).isEqualTo("GPS");
            assertThat(response.getLatitud()).isEqualTo(-12.0464);
            assertThat(response.getLongitud()).isEqualTo(-77.0428);
            assertThat(response.getDireccionManual()).isNull();
        }

        @Test
        @DisplayName("Solo dirección manual → fuenteUbicacion debe ser 'MANUAL'")
        void soloDireccionManual_deberia_asignarFuenteManual() {
            // Arrange
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana López");
            request.setDireccionManual("Av. Lima 123, Miraflores");

            when(alertaRepository.guardar(any())).thenAnswer(inv -> {
                AlertaEmergencia a = inv.getArgument(0);
                return entidadGuardada(a.getFuenteUbicacion(),
                        a.getLatitud(), a.getLongitud(), a.getDireccionManual());
            });

            // Act
            AlertaEmergenciaResponse response = alertaService.crearAlerta(request);

            // Assert
            assertThat(response.getFuenteUbicacion()).isEqualTo("MANUAL");
            assertThat(response.getDireccionManual()).isEqualTo("Av. Lima 123, Miraflores");
            assertThat(response.getLatitud()).isNull();
            assertThat(response.getLongitud()).isNull();
        }

        @Test
        @DisplayName("GPS + dirección manual → fuenteUbicacion debe ser 'GPS_Y_MANUAL'")
        void gpsYManual_deberia_asignarFuenteGpsYManual() {
            // Arrange
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana López");
            request.setLatitud(-12.0464);
            request.setLongitud(-77.0428);
            request.setDireccionManual("Av. Lima 123, Miraflores");

            when(alertaRepository.guardar(any())).thenAnswer(inv -> {
                AlertaEmergencia a = inv.getArgument(0);
                return entidadGuardada(a.getFuenteUbicacion(),
                        a.getLatitud(), a.getLongitud(), a.getDireccionManual());
            });

            // Act
            AlertaEmergenciaResponse response = alertaService.crearAlerta(request);

            // Assert
            assertThat(response.getFuenteUbicacion()).isEqualTo("GPS_Y_MANUAL");
            assertThat(response.getLatitud()).isNotNull();
            assertThat(response.getDireccionManual()).isNotBlank();
        }

        @Test
        @DisplayName("Sin GPS ni dirección manual → debe lanzar IllegalArgumentException")
        void sinNingunaUbicacion_deberia_lanzarExcepcion() {
            // Arrange
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana López");
            // Sin latitud, longitud ni direccionManual

            // Act & Assert
            assertThatThrownBy(() -> alertaService.crearAlerta(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("coordenadas GPS o una dirección manual");

            // El repositorio NO debe ser llamado si la validación falla
            verify(alertaRepository, never()).guardar(any());
        }

        @Test
        @DisplayName("Solo latitud sin longitud → se trata como sin GPS (necesita dirección manual)")
        void soloLatitudSinLongitud_deberia_pedirDireccionManual() {
            // Arrange — tiene latitud pero no longitud → GPS incompleto
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana López");
            request.setLatitud(-12.0464);
            // Sin longitud, sin dirección manual

            // Act & Assert
            assertThatThrownBy(() -> alertaService.crearAlerta(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Estado: ACTIVA → ATENDIDA → RESUELTA
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Ciclo de vida de la alerta")
    class CicloDeVida {

        @Test
        @DisplayName("Alerta recién creada debe tener estado 'ACTIVA'")
        void alertaCreada_debeEstarActiva() {
            AlertaEmergenciaRequest request = new AlertaEmergenciaRequest();
            request.setVictimaId("v-001");
            request.setVictimaNombre("Ana");
            request.setLatitud(-12.04);
            request.setLongitud(-77.04);

            when(alertaRepository.guardar(any())).thenAnswer(inv -> {
                AlertaEmergencia a = inv.getArgument(0);
                a.setId("new-id");
                return a;
            });

            AlertaEmergenciaResponse response = alertaService.crearAlerta(request);

            assertThat(response.getEstado()).isEqualTo("ACTIVA");
            assertThat(response.getCreadoEn()).isNotNull();
        }

        @Test
        @DisplayName("Atender alerta → estado cambia a 'ATENDIDA' y registra profesional")
        void atenderAlerta_debeActualizarEstadoYProfesional() {
            // Arrange — alerta existente en estado ACTIVA
            AlertaEmergencia alertaExistente = entidadGuardada("GPS", -12.04, -77.04, null);
            alertaExistente.setEstado("ACTIVA");

            AtenderAlertaRequest atenderRequest = new AtenderAlertaRequest();
            atenderRequest.setProfesionalId("prof-007");
            atenderRequest.setProfesionalNombre("Dr. García");

            when(alertaRepository.buscarPorId("test-id-123"))
                    .thenReturn(Optional.of(alertaExistente));
            when(alertaRepository.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

            // Act
            AlertaEmergenciaResponse response = alertaService.atenderAlerta("test-id-123", atenderRequest);

            // Assert
            assertThat(response.getEstado()).isEqualTo("ATENDIDA");
            assertThat(response.getAtendidoPorId()).isEqualTo("prof-007");
            assertThat(response.getAtendidoPorNombre()).isEqualTo("Dr. García");
            assertThat(response.getAtendidoEn()).isNotNull();
        }

        @Test
        @DisplayName("Resolver alerta → estado cambia a 'RESUELTA'")
        void resolverAlerta_debeActualizarEstado() {
            AlertaEmergencia alertaExistente = entidadGuardada("MANUAL", null, null, "Calle Falsa 123");
            alertaExistente.setEstado("ATENDIDA");

            when(alertaRepository.buscarPorId("test-id-123"))
                    .thenReturn(Optional.of(alertaExistente));
            when(alertaRepository.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

            AlertaEmergenciaResponse response = alertaService.resolverAlerta("test-id-123");

            assertThat(response.getEstado()).isEqualTo("RESUELTA");
            assertThat(response.getResueltoEn()).isNotNull();
        }

        @Test
        @DisplayName("Atender alerta inexistente → debe lanzar IllegalArgumentException")
        void atenderAlertaInexistente_debeLanzarExcepcion() {
            when(alertaRepository.buscarPorId("id-inexistente"))
                    .thenReturn(Optional.empty());

            AtenderAlertaRequest req = new AtenderAlertaRequest();
            req.setProfesionalId("p-1");
            req.setProfesionalNombre("Prof X");

            assertThatThrownBy(() -> alertaService.atenderAlerta("id-inexistente", req))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("id-inexistente");
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Consultas
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Consultas de alertas")
    class Consultas {

        @Test
        @DisplayName("obtenerActivas() → solo devuelve alertas con estado ACTIVA")
        void obtenerActivas_debeFiltrarCorrectamente() {
            when(alertaRepository.buscarPorEstado("ACTIVA"))
                    .thenReturn(List.of(entidadGuardada("GPS", -12.04, -77.04, null)));

            List<AlertaEmergenciaResponse> activas = alertaService.obtenerActivas();

            assertThat(activas).hasSize(1);
            assertThat(activas.get(0).getEstado()).isEqualTo("ACTIVA");
        }

        @Test
        @DisplayName("obtenerPorVictima() → devuelve solo alertas de esa víctima")
        void obtenerPorVictima_debeDevolverSoloLasSuyas() {
            when(alertaRepository.buscarPorVictima("v-001"))
                    .thenReturn(List.of(entidadGuardada("MANUAL", null, null, "Calle 1")));

            List<AlertaEmergenciaResponse> resultado = alertaService.obtenerPorVictima("v-001");

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getVictimaId()).isEqualTo("v-001");
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Geocodificación inversa
    // ════════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Geocodificación inversa")
    class Geocodificacion {

        @Test
        @DisplayName("geocodificarInverso() sin latitud → debe lanzar IllegalArgumentException")
        void sinLatitud_debeLanzarExcepcion() {
            assertThatThrownBy(() -> alertaService.geocodificarInverso(null, -77.04))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Latitud y longitud son obligatorias");
        }

        @Test
        @DisplayName("geocodificarInverso() sin longitud → debe lanzar IllegalArgumentException")
        void sinLongitud_debeLanzarExcepcion() {
            assertThatThrownBy(() -> alertaService.geocodificarInverso(-12.04, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Latitud y longitud son obligatorias");
        }
    }
}
