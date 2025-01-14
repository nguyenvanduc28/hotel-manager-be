package hotelmanager.demo.models.booking;

import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BookingConsumables extends BaseEntity {

    private Integer consumableId;
    private Integer bookingId;
    private Long totalPrice;
    private Integer quantityUsed;
}
