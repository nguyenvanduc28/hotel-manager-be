package hotelmanager.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consumable_categories")
public class ConsumableCategory extends BaseEntity{

    @NonNull
    @NotEmpty
    private String name;
    private String description;

    @OneToMany(mappedBy = "consumableCategory")
    @JsonBackReference
    private List<Consumable> consumables;
}