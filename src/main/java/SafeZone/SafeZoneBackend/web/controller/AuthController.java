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
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
