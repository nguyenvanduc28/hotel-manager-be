package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.models.enums.EquipmentStatus;

public interface IEquipmentDto {
    Integer getId();
    String getName();
    Integer getRoomId();
    Integer getEquipmentCategoryId();
    Long getInstallationDate();
    String getBarcode();
    EquipmentStatus getStatus();
    String getDescription();
}
