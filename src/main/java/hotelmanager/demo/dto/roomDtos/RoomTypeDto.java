package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomTypeDto extends BaseDto {

    private String name;
    private String description;
    private Integer singleBedCount;
    private Integer doubleBedCount;
    private Boolean extraBedAvailable;
    private String sizeRange;
    private Integer maxOccupancy;
    private Long basePricePerNight;
    private List<RoomPriceDto> roomPrices;
    private Long priceToday;
}
