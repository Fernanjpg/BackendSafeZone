package SafeZone.SafeZoneBackend.domain.service;

import SafeZone.SafeZoneBackend.domain.Repository.RegionesRepository;
import SafeZone.SafeZoneBackend.domain.Repository.UsuariosRepository;
import SafeZone.SafeZoneBackend.domain.dto.RegisterRequest;
import SafeZone.SafeZoneBackend.domain.dto.UpdateUsuarioRequest;
import SafeZone.SafeZoneBackend.domain.dto.UsuarioResponse;
import SafeZone.SafeZoneBackend.persistence.entity.Regiones;
import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuariosService {

    private static final Set<String> ROLES_VALIDOS = Set.of("VICTIM", "PSYCHOLOGIST", "DEFENDER", "ADMIN");

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RegionesRepository regionesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listar() {
        return usuariosRepository.listar().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse obtenerPorId(String id) {
        return toResponse(buscarEntidadPorId(id));
    }

    public Usuarios buscarEntidadPorId(String id) {
        return usuariosRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));
    }

    public Usuarios validarLogin(String email, String password) {
        Usuarios user = usuariosRepository.buscarUsuarioPorEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public Usuarios crearUsuario(RegisterRequest request) {
        validarRol(request.getRoles());

        Usuarios existente = usuariosRepository.buscarUsuarioPorEmail(request.getEmail());
        if (existente != null) {
            throw new IllegalArgumentException("Correo ya registrado");
        }

        if (request.getRegion() == null) {
            throw new IllegalArgumentException("La región es obligatoria");
        }

        Regiones regionReal = validarYResolverRegion(request.getRegion().getId());

        Usuarios user = new Usuarios();
        user.setId(UUID.randomUUID().toString());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTelefono(request.getTelefono());
        user.setRoles(request.getRoles());

        RegionResumen resumen = new RegionResumen();
        resumen.setId(regionReal.getId());
        resumen.setNombre(regionReal.getNombreRegion());
        user.setRegion(resumen);

        user.setEstado("ACTIVO");
        user.setFecharegistro(Instant.now());

        return usuariosRepository.guardar(user);
    }

    public UsuarioResponse crearRespuesta(RegisterRequest request) {
        return toResponse(crearUsuario(request));
    }

    public UsuarioResponse actualizarUsuario(String id, UpdateUsuarioRequest request) {
        Usuarios usuario = buscarEntidadPorId(id);

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellido() != null && !request.getApellido().isBlank()) {
            usuario.setApellido(request.getApellido());
        }
        if (request.getTelefono() != null && !request.getTelefono().isBlank()) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getRoles() != null && !request.getRoles().isBlank()) {
            validarRol(request.getRoles());
            usuario.setRoles(request.getRoles().trim().toUpperCase());
        }
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            usuario.setEstado(request.getEstado().trim().toUpperCase());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRegion() != null && request.getRegion().getId() != null && !request.getRegion().getId().isBlank()) {
            Regiones regionReal = validarYResolverRegion(request.getRegion().getId());
            RegionResumen resumen = new RegionResumen();
            resumen.setId(regionReal.getId());
            resumen.setNombre(regionReal.getNombreRegion());
            usuario.setRegion(resumen);
        }

        return toResponse(usuariosRepository.actualizar(usuario));
    }

    public void eliminarUsuario(String id) {
        if (usuariosRepository.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id);
        }
        usuariosRepository.eliminarPorId(id);
    }

    public void eliminarusuario(Usuarios usuarios) {
        usuariosRepository.eliminar(usuarios);
    }

    private void validarRol(String rol) {
        String rolNormalizado = rol == null ? null : rol.trim().toUpperCase();
        if (rolNormalizado == null || !ROLES_VALIDOS.contains(rolNormalizado)) {
            throw new IllegalArgumentException("Rol no válido");
        }
    }

    private Regiones validarYResolverRegion(String regionId) {
        if (regionId == null || regionId.isBlank()) {
            throw new IllegalArgumentException("La región es obligatoria");
        }

        return regionesRepository.buscarPorId(regionId)
                .orElseThrow(() -> new IllegalArgumentException("Región no encontrada"));
    }

    private UsuarioResponse toResponse(Usuarios usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .roles(usuario.getRoles())
                .estado(usuario.getEstado())
                .region(usuario.getRegion())
                .fecharegistro(usuario.getFecharegistro())
                .build();
    }
}










