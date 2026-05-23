package SafeZone.SafeZoneBackend.domain.dto;

import SafeZone.SafeZoneBackend.persistence.entity.Usuarios;
import SafeZone.SafeZoneBackend.persistence.entity.embebidos.RegionResumen;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UserDto user;

    public static AuthResponse from(String token, Usuarios usuario) {
        UserDto userDto = UserDto.builder()
                .id(usuario.getId())
                .name(usuario.getNombre())
                .lastName(usuario.getApellido())
                .email(usuario.getEmail())
                .role(usuario.getRoles())
                .build();

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }
}