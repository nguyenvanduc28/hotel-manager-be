package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomPriceDto extends BaseDto {
    private Long date;
    private Long price;
    private Integer roomTypeId;
}
