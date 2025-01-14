package hotelmanager.demo.models.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hotelmanager.demo.models.BaseEntity;
import hotelmanager.demo.models.booking.Booking;
import hotelmanager.demo.models.room.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking_rooms")
public class BookingRoom extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;
}
