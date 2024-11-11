package hotelmanager.demo.dto.bookingDtos;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.dto.roomDtos.ConsumableCategoryDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingConsumableDto extends BaseDto {
    private String name;
    private ConsumableCategoryDto consumableCategory;
    private String unit;
    private Long expiryDate;
    private String barcode;
    private String description;
    private Integer quantityUsed;
    private Long totalPrice;
    private Integer consumableId;
}
