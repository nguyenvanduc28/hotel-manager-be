package hotelmanager.demo.dto.bookingDtos;

public interface IBookingDto {
    Integer getId();
    Long getCheckInDate();
    Long getCheckInTime();
    Long getCheckOutTime();
    Long getCheckOutDate();
    Long getConfirmedTime();
    Long getEstimatedArrivalTime();
    Long getBookingDate();
    Boolean getIsGroup();
    Double getTotalCost();
    String getStatus();
    Double getDeposit();
    String getCancellationPolicy();
    Long getCanceledAt();
    Integer getNumberOfAdults();
    Integer getNumberOfChildren();
    Boolean getIsGuaranteed();
}
