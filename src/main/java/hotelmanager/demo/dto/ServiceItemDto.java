package hotelmanager.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceItemDto extends BaseDto{
    private String name;
    private Long price;
    private String image;
    private String description;
    private ServiceDto serviceType;
}
