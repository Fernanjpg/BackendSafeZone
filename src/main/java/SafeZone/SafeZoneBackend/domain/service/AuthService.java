package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.config.JwtService;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.AuthResponse;
import SafeZone.SafeZoneBackend.domain.dto.LoginRequest;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
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

        if (usuariosRepository.buscarUsuarioPorEmailYRol(request.getEmail(), rolFormateado) != null) {
            throw new RuntimeException("El correo electrónico ya se encuentra registrado con este rol.");
        }

        Usuarios usuario = new Usuarios();
        usuario.setId(java.util.UUID.randomUUID().toString());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setRoles(rolFormateado);
        usuario.setEstado("ACTIVO");
        usuario.setFecharegistro(java.time.Instant.now());

        // --- CORRECCIÓN AQUÍ ---
        if (request.getRegion() != null) {
            RegionResumen region = new RegionResumen();
            region.setId(request.getRegion().getId());
            region.setNombre(request.getRegion().getNombre());
            usuario.setRegion(region);
        } else {
            throw new RuntimeException("La región es obligatoria.");
        }

        System.out.println("DEBUG: Objeto usuario a guardar: " + usuario.toString());

        Usuarios usuarioGuardado = usuariosRepository.guardar(usuario);

        String token = generarTokenParaUsuario(usuarioGuardado);
        return generarAuthResponse(usuarioGuardado, token);
    }

    public AuthResponse login(LoginRequest request) {
        Usuarios usuario = usuariosRepository.buscarUsuarioPorEmail(request.getEmail());

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado en la base de datos.");
        }

        // LOG DE SEGURIDAD PARA DEPURAR
        System.out.println("DEBUG: Password obtenido de BD: " + usuario.getPassword());
        System.out.println("DEBUG: Password recibido en JSON: " + request.getPassword());

        boolean matches = passwordEncoder.matches(request.getPassword(), usuario.getPassword());

        if (!matches) {
            System.out.println("DEBUG: ¡La comparación de BCrypt falló!");
            throw new RuntimeException("Credenciales incorrectas.");
        }


        // 4. Comparamos contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas.");
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