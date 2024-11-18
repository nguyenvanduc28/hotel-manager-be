package hotelmanager.demo.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hotel extends BaseEntity{
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
    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả
    private String logoUrl; // URL logo
    private Integer totalStaff; // Số nhân sự
    private String ownerName; // Tên chủ sở hữu
    private String status; // Trạng thái
    private String address; // Địa chỉ

    // Tiện nghi cơ bản
    @Column(columnDefinition = "boolean default false")
    private Boolean hasWifi; // Có Wifi
    @Column(columnDefinition = "boolean default false")
    private Boolean hasParking; // Có bãi đậu xe
    @Column(columnDefinition = "boolean default false")
    private Boolean hasRestaurant; // Có nhà hàng
    @Column(columnDefinition = "boolean default false")
    private Boolean hasSwimmingPool; // Có hồ bơi
    @Column(columnDefinition = "boolean default false")
    private Boolean hasConferenceRoom; // Có phòng hội nghị
    @Column(columnDefinition = "boolean default false")
    private Boolean has24HourFrontDesk; // Có lễ tân 24 giờ
    @Column(columnDefinition = "boolean default false")
    private Boolean hasBar; // Có quầy bar
    @Column(columnDefinition = "boolean default false")
    private Boolean hasElevator; // Có thang máy
    @Column(columnDefinition = "boolean default false")
    private Boolean hasAirConditioning; // Có điều hòa
    @Column(columnDefinition = "boolean default false")
    private Boolean hasShuttle; // Có chuyến xe đưa đón
    
    private String otherAmenities; // Tiện ích khác
}
