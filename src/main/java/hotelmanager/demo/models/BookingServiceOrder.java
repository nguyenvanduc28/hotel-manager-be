package hotelmanager.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BookingServiceOrder extends BaseEntity {
    private Integer bookingServiceId;
    private Long totalPrice;
    private Long orderCreatedAt;
    private Long servicedAt;
    private String status = "Mới";
    private String note;
    private Integer serviceTypeId;
}
