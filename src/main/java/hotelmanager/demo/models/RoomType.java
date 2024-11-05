package hotelmanager.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_types")
public class RoomType extends BaseEntity{

    private String name;
    private String description;
    private Integer singleBedCount;
    private Integer doubleBedCount;
    private Boolean extraBedAvailable;
    private String sizeRange;
    private Integer maxOccupancy;

    @Column(columnDefinition = "bigint default 0")
    private Long basePricePerNight;

    @OneToMany(mappedBy = "roomType")
    @JsonBackReference
    private List<Room> rooms;
}
