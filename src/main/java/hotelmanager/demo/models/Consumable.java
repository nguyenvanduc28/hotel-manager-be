package hotelmanager.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumables")
public class Consumable extends BaseEntity{

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