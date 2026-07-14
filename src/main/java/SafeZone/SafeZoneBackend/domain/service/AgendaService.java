package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.AgendaRepository;
import SafeZone.SafeZoneBackend.persistence.entity.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgendaService {

private final AgendaRepository agendaRepository;
@Autowired
public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }


    public Agenda guardarEvento(Agenda evento, String rolUsuario) {
        if (evento.getId() == null || evento.getId().trim().isEmpty()) {
            evento.setId(UUID.randomUUID().toString());
        }

        // 1. Extraemos el tipo y el rol como texto limpio en mayúsculas
        String tipoEvento = (evento.getTipo() != null) ? evento.getTipo().toUpperCase().trim() : "";
        String rolClean = (rolUsuario != null) ? rolUsuario.toUpperCase().trim() : "";

        // 🛡️ Reglas de negocio estrictas por Rol usando Strings

        // Si es VÍCTIMA, obligatoriamente el tipo debe ser CITA_PSICOLOGICA
        if ("VICTIM".equals(rolClean)) {
            if (!"CITA_PSICOLOGICA".equals(tipoEvento)) {
                throw new IllegalArgumentException("Las víctimas solo tienen permitido agendar Citas Psicológicas.");
            }
        }

        // Si es una AUDIENCIA, solo la puede crear el ADMIN o JUEZ
        if ("AUDIENCIA".equals(tipoEvento) || "AUDIENCIA_JUDICIAL".equals(tipoEvento)) {
            if (!"ADMIN".equals(rolClean) && !"DEFENDER".equals(rolClean)) {
                throw new IllegalArgumentException("Solo los administradores o defensores pueden programar audiencias judiciales.");
            }
        }

        // Validación: Verificar entre cruces antes de guardar usando Strings en el Repositorio
        List<Agenda> existentes = agendaRepository.AgendarCitas(
                evento.getUsuarioid(),
                evento.getFechaInicio().minusMinutes(1).toString(),
                evento.getFechaFin().plusMinutes(1).toString()
        );

        if (!existentes.isEmpty()) {
            throw new IllegalStateException("Ya existe un evento programado en este horario.");
        }

        // 2. Usamos el método nativo .save() de CosmosRepository
        return agendaRepository.guardar(evento);
    }
    // 2. Obtener las citas de un psicólogo específico (Consumiendo tu clase)
    public List<Agenda> obtenerCitasPorPsicologo(String profesionalId) {
        return agendaRepository.listarPorPsicologoDirecto(profesionalId);
    }

    // 3. El psicólogo gestiona la cita y adjunta el enlace virtual
    public Agenda gestionarCitaPorPsicologo(String citaId, String nuevoEstado, String linkReunion, String profesionalIdAuth) {
        // Buscamos a través de tu clase repositorio
        Optional<Agenda> agendaOpt = agendaRepository.findById(citaId);

        if (agendaOpt.isEmpty()) {
            throw new IllegalArgumentException("La cita no existe.");
        }

        Agenda agenda = agendaOpt.get();

        // Verificamos que el psicólogo asignado en Cosmos DB sea quien está logueado
        if (!agenda.getProfesionalId().equals(profesionalIdAuth)) {
            throw new SecurityException("No estás autorizado para gestionar esta cita.");
        }

        String estadoUpper = nuevoEstado.toUpperCase().trim();
        if (!List.of("ACEPTADA", "RECHAZADA", "COMPLETADA").contains(estadoUpper)) {
            throw new IllegalArgumentException("Estado no válido para la gestión de la cita.");
        }

        agenda.setEstado(estadoUpper);

        if (linkReunion != null && !linkReunion.trim().isEmpty()) {
            agenda.setLinkReunion(linkReunion.trim());
        }

        return agendaRepository.guardar(agenda);
    }


    public List<Agenda> obtenerAgendaCompletaPorUsuario(String usuarioid) {
        if (usuarioid == null || usuarioid.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario no puede estar vacío.");
        }

        return agendaRepository.listarPorVictimaDirecto(usuarioid);
    }
}
