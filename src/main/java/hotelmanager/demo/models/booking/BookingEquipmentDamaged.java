package hotelmanager.demo.models.booking;

import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class BookingEquipmentDamaged extends BaseEntity {
    private Integer equipmentId;
    private Integer bookingId;
    private Long damageFee;
    private String damageDescription;
}
