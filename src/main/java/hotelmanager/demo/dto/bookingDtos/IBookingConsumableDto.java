package hotelmanager.demo.dto.bookingDtos;

public interface IBookingConsumableDto {
    Integer getId();
    String getName();
    Integer getConsumableCategoryId();
    String getUnit();
    Long getExpiryDate();
    String getBarcode();
    String getDescription();
    Integer getQuantityUsed();
    Long getTotalPrice();
    Integer getConsumableId();
} 