package hotelmanager.demo.models.room;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumable_categories")
public class ConsumableCategory extends BaseEntity {

    @NonNull
    private String name;
    private String description;

    @OneToMany(mappedBy = "consumableCategory")
    @JsonBackReference
    private List<Consumable> consumables;
}