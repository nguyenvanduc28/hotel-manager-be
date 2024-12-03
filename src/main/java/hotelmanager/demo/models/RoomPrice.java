package hotelmanager.demo.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_prices")
public class RoomPrice extends BaseEntity{
    private Long date;
    private Long price;
    private Integer roomTypeId;
}
