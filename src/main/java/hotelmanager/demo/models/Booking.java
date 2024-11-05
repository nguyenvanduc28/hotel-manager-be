package hotelmanager.demo.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    private Long checkInDate;
    private Long checkOutDate;
    private Long checkInTime;
    private Long checkOutTime;
    private Long estimatedArrivalTime;
    private Long bookingDate;
    private Boolean isGroup;
    private Double totalCost;
    private String status;
    private Double deposit;
    private String cancellationPolicy;
    private Long canceledAt;
    private Boolean isGuaranteed;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    @OneToMany(mappedBy = "booking")
    private List<BookingRoom> bookingRooms;

//    @OneToMany(mappedBy = "booking")
//    @JsonManagedReference
//    private List<BookingConsumable> bookingConsumables;
//
//    @OneToMany(mappedBy = "booking")
//    @JsonManagedReference
//    private List<BookingEquipment> bookingEquipment;
}