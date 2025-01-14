package hotelmanager.demo.models.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hotelmanager.demo.models.BaseEntity;
import hotelmanager.demo.models.room.ConsumableCategory;
import hotelmanager.demo.models.room.Room;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumables")
public class Consumable extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "consumable_category_id")
    private ConsumableCategory consumableCategory;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    @Column(columnDefinition = "bigint default 0")
    private Long price;

    private Integer quantity;
    private String unit;
    private Long expiryDate;
    private String barcode;
    private String description;
}