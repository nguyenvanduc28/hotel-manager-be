package hotelmanager.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "equipment_categories")
public class EquipmentCategory extends BaseEntity{

    private String name;
    private String description;

    @OneToMany(mappedBy = "equipmentCategory")
    @JsonBackReference
    private List<Equipment> equipmentList;
}