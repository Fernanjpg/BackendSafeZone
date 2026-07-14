package SafeZone.SafeZoneBackend.web.controller;


import SafeZone.SafeZoneBackend.domain.dto.LoginRequest;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.domain.dto.UpdateUsuarioRequest;
import SafeZone.SafeZoneBackend.domain.dto.UsuarioResponse;
import SafeZone.SafeZoneBackend.domain.service.UsuariosService;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class Usuarioscontroller {

@Autowired
private UsuariosService usuariosService;

     @GetMapping
     @PreAuthorize("hasRole('ADMIN')")
     public List<UsuarioResponse> listar() {
        return usuariosService.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse obtenerPorId(@PathVariable String id) {
        return usuariosService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(usuariosService.crearRespuesta(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse actualizar(@PathVariable String id, @RequestBody UpdateUsuarioRequest request) {
        return usuariosService.actualizarUsuario(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuariosService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> registrarLegacy(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(usuariosService.crearRespuesta(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarusuario(@RequestBody Usuarios usuarios) {
        usuariosService.eliminarusuario(usuarios);
    }

    // Lista solo administradores y gestores (no todos los usuarios del sistema)
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponse> listarAdminsYGestores() {
        return usuariosService.listarPorRoles(List.of("ADMIN", "GESTOR"));
    }

    // Desactiva (no borra) un usuario: cambia estado a INACTIVO
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse desactivar(@PathVariable String id) {
        return usuariosService.desactivarUsuario(id);
    }

    //GET/api/usuarios/defender
    @GetMapping("/defender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuarios>> ListarDefensoresLegales() {
        List<Usuarios> defensores = usuariosService.buscarPorRoLDefensorLegal("DEFENDER");
        return ResponseEntity.ok(defensores);
    }
    @GetMapping("/psyphocolyst")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuarios>> ListarPsicologos () {
         List<Usuarios> psicologos = usuariosService.buscarPorPsicologo("PSYCHOLOGIST");
         return ResponseEntity.ok(psicologos);
    }
}






