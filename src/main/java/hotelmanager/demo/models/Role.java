package hotelmanager.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import hotelmanager.demo.models.enums.RoleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<UserEntity> userEntities;
}
