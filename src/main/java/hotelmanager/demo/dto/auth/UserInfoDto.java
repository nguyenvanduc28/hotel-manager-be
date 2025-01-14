package hotelmanager.demo.dto.auth;

import hotelmanager.demo.models.auth.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfoDto {

    private String username;

    private List<Role> roles;
}
