package hotelmanager.demo.dto.bookingDtos;

import hotelmanager.demo.dto.roomDtos.EquipmentCategoryDto;
import hotelmanager.demo.models.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingEquipmentDamagedDto extends BaseEntity {
    private String name;
    private EquipmentCategoryDto equipmentCategory;
    private Long installationDate;
    private String barcode;
    private String description;
    private Long damageFee;
    private String damageDescription;
    private Integer equipmentId;
}
