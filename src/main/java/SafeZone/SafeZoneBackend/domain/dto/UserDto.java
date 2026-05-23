package SafeZone.SafeZoneBackend.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String name;
    private String lastName;
    private String email;
    private String role;
}
