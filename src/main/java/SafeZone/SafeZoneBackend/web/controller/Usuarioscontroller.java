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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuarios")
public class Usuarioscontroller {

@Autowired
private UsuariosService usuariosService;

     @GetMapping
     public List<UsuarioResponse> listar() {
        return usuariosService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(@PathVariable String id) {
        return usuariosService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(usuariosService.crearRespuesta(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable String id, @RequestBody UpdateUsuarioRequest request) {
        return usuariosService.actualizarUsuario(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuariosService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrarLegacy(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(usuariosService.crearRespuesta(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/eliminar")
    public void eliminarusuario(@RequestBody Usuarios usuarios) {
        usuariosService.eliminarusuario(usuarios);
    }




}






