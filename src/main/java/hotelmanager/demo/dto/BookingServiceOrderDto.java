package hotelmanager.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingServiceOrderDto extends BaseDto {
    private Integer bookingServiceId;
    private List<OrderItemDto> orderItems;
    private Long totalPrice;
    private Long orderCreatedAt;
    private Long servicedAt;
    private String status;
    private String note;
}
