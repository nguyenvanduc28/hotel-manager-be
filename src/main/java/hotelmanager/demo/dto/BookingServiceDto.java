package hotelmanager.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingServiceDto extends BaseDto {
    private Integer bookingId;
    private List<BookingServiceOrderDto> serviceOrders;
    private Long totalPrice;
}
