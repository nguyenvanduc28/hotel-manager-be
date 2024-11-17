package hotelmanager.demo.dto;

import hotelmanager.demo.dto.bookingDtos.ImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelDto extends BaseDto{
    private String name; // Tên khách sạn
    private Double rating; // Đánh giá khách sạn
    private String city; // Thành phố
    private String postalCode; // Mã vùng
    private String country; // Quốc gia
    private Double latitude; // Vĩ độ
    private Double longitude; // Kinh độ
    private String phoneNumber; // Số điện thoại
    private String email; // Email
    private String websiteUrl; // URL website
    private Integer numberOfRooms; // Số phòng
    private String checkInTime; // Giờ vào
    private String checkOutTime; // Giờ ra
    private String description; // Mô tả
    private String logoUrl; // URL logo
    private Integer totalStaff; // Số nhân sự
    private String ownerName; // Tên chủ sở hữu
    private String status; // Trạng thái
    private String address; // Địa chỉ
    private List<ImageDto> images;
    //tiện ích
    private Boolean hasWifi; // Có Wifi
    private Boolean hasParking; // Có bãi đậu xe
    private Boolean hasRestaurant; // Có nhà hàng
    private Boolean hasSwimmingPool; // Có hồ bơi
    private Boolean hasConferenceRoom; // Có phòng hội nghị
    private Boolean has24HourFrontDesk; // Có lễ tân 24 giờ
    private Boolean hasBar; // Có quầy bar
    private Boolean hasElevator; // Có thang máy
    private Boolean hasAirConditioning; // Có điều hòa
    private Boolean hasShuttle; // Có chuyến xe đưa đón
    
    private String otherAmenities; // Tiện ích khác

}
