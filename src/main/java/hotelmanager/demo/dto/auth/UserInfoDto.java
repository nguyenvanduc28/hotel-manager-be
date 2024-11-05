package hotelmanager.demo.dto.auth;

import hotelmanager.demo.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class UserInfoDto {

    private String username;

    private List<Role> roles;
}
