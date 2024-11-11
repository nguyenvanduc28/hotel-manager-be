package hotelmanager.demo.dto.roomDtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.models.EquipmentCategory;
import hotelmanager.demo.models.Room;
import hotelmanager.demo.models.enums.EquipmentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDto extends BaseDto {
    private String name;

    private EquipmentCategoryDto equipmentCategory;

    private RoomDto room;

    private Long installationDate;
    private String barcode;

    private EquipmentStatus status;
    private String description;
}
