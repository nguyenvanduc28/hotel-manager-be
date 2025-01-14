package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableCategoryDto extends BaseDto {
    private String name;
    private String description;
}
