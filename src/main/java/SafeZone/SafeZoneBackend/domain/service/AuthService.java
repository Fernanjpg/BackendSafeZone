package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.config.JwtService;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
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

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registrar(RegisterRequest request) {
        String rolFormateado = request.getRoles().toUpperCase();

        // Usamos tu repositorio intermedio para validar si ya existe
        if (usuariosRepository.buscarUsuarioPorEmailYRol(request.getEmail(), rolFormateado) != null) {
            throw new RuntimeException("El correo electrónico ya se encuentra registrado con este rol.");
        }

        // Mapeo limpio hacia tu entidad Usuarios
        Usuarios usuario = new Usuarios();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        // Encriptación profesional de la contraseña
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setRoles(rolFormateado);
        usuario.setRegion(request.getRegion());

        // Guardamos usando tu método estructurado
        Usuarios usuarioGuardado = usuariosRepository.guardar(usuario);

        String token = generarTokenParaUsuario(usuarioGuardado);
        return generarAuthResponse(usuarioGuardado, token);
    }

    public AuthResponse login(LoginRequest request) {
        // Buscamos usando tu método original buscarUsuarioPorEmail
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(request.getEmail());

        if (usuario == null) {
            throw new RuntimeException("Credenciales incorrectas o el usuario no existe.");
        }

        // Verificación del hash de la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas.");
        }

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
        return new AuthResponse(
                token,
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRoles(),
                usuario.getRegion()
        );
    }
}