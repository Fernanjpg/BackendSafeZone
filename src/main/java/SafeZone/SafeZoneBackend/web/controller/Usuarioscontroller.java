package SafeZone.SafeZoneBackend.web.controller;


import SafeZone.SafeZoneBackend.domain.dto.LoginRequest;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.domain.service.UsuariosService;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
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

     @PostMapping("/login")
     public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
         Usuarios usuario = usuariosService.validarLogin(
                 loginRequest.getEmail(),
                 loginRequest.getPassword()
         );

         if (usuario != null) {
             // Login exitoso: puedes devolver el objeto usuario o un mensaje
             return ResponseEntity.ok(usuario);
         } else {
             // Login fallido
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
         }
     }



    @GetMapping("/listar")
     public List<Usuarios> listar ()  {
        return usuariosService.listar();
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuarios> registrar(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(usuariosService.registrar(request));
    }

    @DeleteMapping("/eliminar")
    public void eliminarusuario (@RequestBody Usuarios usuarios) {
        usuariosService.eliminarusuario(usuarios);

    }




}






