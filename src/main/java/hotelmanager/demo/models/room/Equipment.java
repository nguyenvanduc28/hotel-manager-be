package hotelmanager.demo.models.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hotelmanager.demo.models.BaseEntity;
import hotelmanager.demo.models.enums.EquipmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment")
public class Equipment extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "equipment_category_id")
    private EquipmentCategory equipmentCategory;

    @ManyToOne
    @JoinColumn(name = "room_id")
//    @JsonBackReference
    @JsonIgnore
    private Room room;

    private Long installationDate;
    private String barcode;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    private String description;
}