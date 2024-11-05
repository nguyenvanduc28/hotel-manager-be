package hotelmanager.demo.dto.auth;

import hotelmanager.demo.models.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AuthDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
    @NotEmpty
    private List<RoleDto> roles;
}
