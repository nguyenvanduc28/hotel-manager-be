package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.models.Consumable;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ConsumableCategoryDto extends BaseDto {
    @NonNull
    @NotEmpty
    private String name;
    private String description;
}
