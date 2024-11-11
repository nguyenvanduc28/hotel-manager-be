package hotelmanager.demo.dto.bookingDtos;

public interface IBookingEquipmentDamagedDto {
    Integer getId();
    String getName();
    Integer getEquipmentCategoryId();
    Long getInstallationDate();
    String getBarcode();
    String getDescription();
    Long getDamageFee();
    String getDamageDescription();
    Integer getEquipmentId();
} 