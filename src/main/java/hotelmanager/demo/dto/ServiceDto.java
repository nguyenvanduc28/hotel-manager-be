package hotelmanager.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDto extends BaseDto{
    private String name;
    private String serviceType;
    private String description;
}
