package hotelmanager.demo.dto.roomDtos;

public interface IConsumableDto {
    Integer getId();
    String getName();
    Integer getRoomId();
    Integer getConsumableCategoryId();
    Long getPrice();
    Integer getQuantity();
    String getUnit();
    Long getExpiryDate();
    String getBarcode();
    String getDescription();
}
