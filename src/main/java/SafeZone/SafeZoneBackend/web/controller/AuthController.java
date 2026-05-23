package SafeZone.SafeZoneBackend.web.controller;

import SafeZone.SafeZoneBackend.domain.dto.AuthResponse;
import SafeZone.SafeZoneBackend.domain.dto.LoginRequest;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.domain.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registrar nuevos usuarios en SafeZone.
     * Usa @Valid para asegurar que si el email está mal formado o faltan campos,
     * Spring responda un 400 Bad Request inmediatamente sin gastar recursos procesando.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.registrar(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("--- ERROR EN REGISTRO ---");
            e.printStackTrace(); // Esto DEBE salir en la consola de IntelliJ ahora sí o sí
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.toString());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("--- ERROR EN LOGIN ---");
            e.printStackTrace(); // Esto DEBE salir en la consola de IntelliJ ahora sí o sí
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.toString());
        }

    }
}
