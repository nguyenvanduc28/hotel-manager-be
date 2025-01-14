package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumableDto extends BaseDto {

    private String name;

    private ConsumableCategoryDto consumableCategory;
    private RoomDto room;
    private Long price;
    private Integer quantity;
    private String unit;
    private Long expiryDate;
    private String barcode;
    private String description;
}
