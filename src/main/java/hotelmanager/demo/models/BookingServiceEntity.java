package hotelmanager.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BookingServiceEntity extends BaseEntity {
    private Integer bookingId;
    private Long totalPrice;
}
