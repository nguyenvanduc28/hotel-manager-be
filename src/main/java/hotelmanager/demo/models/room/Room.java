package hotelmanager.demo.models.room;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "rooms")
public class Room extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    @JsonManagedReference
    private RoomType roomType;

    private String roomNumber;

    private Integer floor;
    private Integer size;

    @Column(columnDefinition = "boolean default true")
    private Boolean isAvailable;

    @Column(columnDefinition = "boolean default false")
    private Boolean isSmokingAllowed;

    private Boolean hasPrivateKitchen;
    private Boolean hasPrivateBathroom;
    private Boolean hasBalcony;
    private Boolean hasLakeView;
    private Boolean hasGardenView;
    private Boolean hasPoolView;
    private Boolean hasMountainView;
    private Boolean hasLandmarkView;
    private Boolean hasCityView;
    private Boolean hasRiverView;
    private Boolean hasCourtyardView;
    private Boolean hasFreeWifi;
    private Boolean hasSoundproofing;

    private String description;
    private String images;

    @OneToMany(mappedBy = "room")
//    @JsonManagedReference
    private List<Consumable> consumables;

    @OneToMany(mappedBy = "room")
//    @JsonManagedReference
    private List<Equipment> equipmentList;
}