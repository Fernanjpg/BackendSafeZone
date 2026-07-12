package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.MensajeRequest;
import SafeZone.SafeZoneBackend.domain.dto.MensajeResponse;
import SafeZone.SafeZoneBackend.domain.service.MensajesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * RF-07 – Chat de Mensajería Multidisciplinario
 *
 * Endpoints:
 *   GET    /api/mensajes/{denunciaid}            → listar mensajes de una conversación
 *   POST   /api/mensajes/{denunciaid}            → enviar mensaje
 *   PATCH  /api/mensajes/{denunciaid}/leer       → marcar como leídos
 *   GET    /api/mensajes/no-leidos/count         → total mensajes no leídos del usuario
 */
@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174",
        "http://localhost:5175"
})
public class MensajesController {

    @Autowired
    private MensajesService mensajesService;

    /**
     * Listar todos los mensajes de una denuncia/conversación.
     * Acceso: VICTIM, PSYCHOLOGIST, DEFENDER, ADMIN (validado en service).
     */
    @GetMapping("/{denunciaid}")
    public ResponseEntity<List<MensajeResponse>> listarMensajes(
            @PathVariable String denunciaid,
            @AuthenticationPrincipal String usuarioId) {

        List<MensajeResponse> mensajes = mensajesService.listarMensajes(denunciaid, usuarioId);
        return ResponseEntity.ok(mensajes);
    }

    /**
     * Enviar un mensaje dentro de una denuncia.
     * El remitente es el usuario autenticado (JWT).
     */
    @PostMapping("/{denunciaid}")
    public ResponseEntity<MensajeResponse> enviarMensaje(
            @PathVariable String denunciaid,
            @Valid @RequestBody MensajeRequest request,
            @AuthenticationPrincipal String usuarioId) {

        MensajeResponse response = mensajesService.enviarMensaje(denunciaid, request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Marcar como leídos todos los mensajes dirigidos al usuario en una denuncia.
     */
    @PatchMapping("/{denunciaid}/leer")
    public ResponseEntity<Void> marcarComoLeidos(
            @PathVariable String denunciaid,
            @AuthenticationPrincipal String usuarioId) {

        mensajesService.marcarComoLeidos(denunciaid, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Contar mensajes no leídos del usuario autenticado (para el badge del botón de chat).
     */
    @GetMapping("/no-leidos/count")
    public ResponseEntity<Map<String, Integer>> contarNoLeidos(
            @AuthenticationPrincipal String usuarioId) {

        int total = mensajesService.contarNoLeidos(usuarioId);
        return ResponseEntity.ok(Map.of("total", total));
    }
}