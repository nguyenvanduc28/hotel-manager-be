package hotelmanager.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemDto extends BaseDto {
    private Integer orderId;
    private ServiceItemDto serviceItem;
    private Long totalPrice;
    private Integer quantity;
}
