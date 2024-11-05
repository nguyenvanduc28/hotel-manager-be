package hotelmanager.demo.dto.auth;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.models.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto extends BaseDto {
    private RoleType name;
}
