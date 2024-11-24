package hotelmanager.demo.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderItem extends BaseEntity {
    private Integer orderId;
    private Integer serviceItemId;
    private Long totalPrice;
    private Integer quantity;
}
