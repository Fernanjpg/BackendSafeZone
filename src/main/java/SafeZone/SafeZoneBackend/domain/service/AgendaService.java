package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.AgendaRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AgendaService {

private final AgendaRepository agendaRepository;

public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }
    public Agenda guardarEvento(Agenda evento) {
        if (evento.getId() == null || evento.getId().trim().isEmpty()) {
            evento.setId(UUID.randomUUID().toString());
        }
        // Validación: Verificar solapamiento antes de guardar
        List<Agenda> existentes = agendaRepository.AgendarCitas(



                evento.getUsuarioid(),
                evento.getFechaInicio().minusMinutes(1),
                evento.getFechaFin().plusMinutes(1)
        );

        if (!existentes.isEmpty()) {
            throw new IllegalStateException("Ya existe un evento programado en este horario.");
        }

        return agendaRepository.guardar(evento);
    }

    public List<Agenda> obtenerAgenda(String usuarioid, LocalDateTime start, LocalDateTime end) {
        return agendaRepository.AgendarCitas(usuarioid, start, end);
    }

    public Agenda actualizarEstadoEvento(String id, String usuarioid, String nuevoEstado) {
        // 1. Buscar el evento existente en Cosmos DB
        Agenda evento = agendaRepository.BuscarporId(id)
                .orElseThrow(() -> new IllegalArgumentException("El evento no existe con el ID: " + id));

        // 2. Validar que el usuario sea el propietario (Seguridad)
        if (!evento.getUsuarioid().equals(usuarioid)) {
            throw new SecurityException("No tienes permisos para modificar este evento.");
        }

        // 3. Actualizar solo el campo estado (puedes mapearlo a tu Enum si lo usas)
        evento.setEstado(nuevoEstado);

        return agendaRepository.guardar(evento);
    }

    public Agenda actualizarFechasEvento(String id, String usuarioid, LocalDateTime nuevaFechaInicio, LocalDateTime nuevaFechaFin) {
        // 1. Buscar el evento
        Agenda evento = agendaRepository.BuscarporId(id)
                .orElseThrow(() -> new IllegalArgumentException("El evento no existe con el ID: " + id));

        // 2. Validar propietario
        if (!evento.getUsuarioid().equals(usuarioid)) {
            throw new SecurityException("No tienes permisos para modificar este evento.");
        }

        // 3. Actualizar rangos de tiempo
        evento.setFechaInicio(nuevaFechaInicio);
        evento.setFechaFin(nuevaFechaFin);

        return agendaRepository.guardar(evento);
    }





}
