package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.AlertaEmergenciaRepository;
import SafeZone.SafeZoneBackend.domain.dto.AlertaEmergenciaRequest;
import SafeZone.SafeZoneBackend.domain.dto.AtenderAlertaRequest;
import SafeZone.SafeZoneBackend.persistence.entity.AlertaEmergencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AlertaEmergenciaService {

    @Autowired
    private AlertaEmergenciaRepository alertaRepository;

    // Víctima activa el botón de pánico — guardamos todo lo que mandó
    public AlertaEmergencia crearAlerta(AlertaEmergenciaRequest request) {
        AlertaEmergencia alerta = new AlertaEmergencia();

        alerta.setId(UUID.randomUUID().toString());
        alerta.setVictimaId(request.getVictimaId());
        alerta.setVictimaNombre(request.getVictimaNombre());
        alerta.setVictimaEmail(request.getVictimaEmail());

        // GPS — guardamos solo si vino en el request
        alerta.setLatitud(request.getLatitud());
        alerta.setLongitud(request.getLongitud());
        alerta.setPrecision(request.getPrecision());

        alerta.setDireccionManual(request.getDireccionManual());
        alerta.setMensaje(request.getMensaje());

        alerta.setEstado("ACTIVA");
        alerta.setCreadoEn(Instant.now());

        return alertaRepository.guardar(alerta);
    }

    // Devuelve solo las activas para que los profesionales las vean
    public List<AlertaEmergencia> obtenerActivas() {
        return alertaRepository.buscarPorEstado("ACTIVA");
    }

    // Historial completo
    public List<AlertaEmergencia> obtenerTodas() {
        return alertaRepository.listarTodas();
    }

    // Profesional marca la alerta como atendida
    public AlertaEmergencia atenderAlerta(String alertaId, AtenderAlertaRequest request) {
        AlertaEmergencia alerta = alertaRepository.buscarPorId(alertaId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada: " + alertaId));

        alerta.setEstado("ATENDIDA");
        alerta.setAtendidoPorId(request.getProfesionalId());
        alerta.setAtendidoPorNombre(request.getProfesionalNombre());
        alerta.setAtendidoEn(Instant.now());

        return alertaRepository.guardar(alerta);
    }

    // Marcar como resuelta — cierre del caso de emergencia
    public AlertaEmergencia resolverAlerta(String alertaId) {
        AlertaEmergencia alerta = alertaRepository.buscarPorId(alertaId)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada: " + alertaId));

        alerta.setEstado("RESUELTA");
        alerta.setResueltoEn(Instant.now());

        return alertaRepository.guardar(alerta);
    }
}
