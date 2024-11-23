package hotelmanager.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingServiceItemDto extends BaseDto {
    private Integer bookingId;
    private ServiceItemDto serviceItem;
    private Long totalPrice;
    private Integer quantity;
    private String note;
}
