package hotelmanager.demo.models;

import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Invoice extends BaseEntity {
    private Double totalAmount;
    private String paymentMethod;
    private Integer bookingId;
    private Integer customerId;
    private Long issueDate;
    private String paymentStatus;
}
