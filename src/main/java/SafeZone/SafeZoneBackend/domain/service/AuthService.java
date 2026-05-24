package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.config.JwtService;
import SafeZone.SafeZoneBackend.domain.dto.AuthResponse;
import SafeZone.SafeZoneBackend.domain.dto.LoginRequest;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuariosService usuariosService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registrar(RegisterRequest request) {
        Usuarios usuarioGuardado = usuariosService.crearUsuario(request);

        String token = generarTokenParaUsuario(usuarioGuardado);
        return generarAuthResponse(usuarioGuardado, token);
    }

    public AuthResponse login(LoginRequest request) {
        Usuarios usuario = usuariosService.validarLogin(request.getEmail(), request.getPassword());

        if (usuario == null) {
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }

        // 5. Todo OK, generamos token
        String token = generarTokenParaUsuario(usuario);
        return generarAuthResponse(usuario, token);
    }

    private String generarTokenParaUsuario(Usuarios usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rol", usuario.getRoles());
        extraClaims.put("nombreCompleto", usuario.getNombre() + " " + usuario.getApellido());

        return jwtService.generateToken(extraClaims, usuario.getEmail());
    }

    private AuthResponse generarAuthResponse(Usuarios usuario, String token) {
        return AuthResponse.from(token, usuario);
    }
}