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

}
