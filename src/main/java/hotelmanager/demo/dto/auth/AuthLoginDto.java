package hotelmanager.demo.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginDto {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
