package hotelmanager.demo.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
@Entity
@Setter
@Getter
public class BookingServiceItem extends BaseEntity {
    private Integer bookingId;
    private Integer serviceItemId;
    private String note;
    private Long totalPrice;
    private Integer quantity;

}
