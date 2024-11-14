package hotelmanager.demo.models;
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
    private String description; // Mô tả
    private String logoUrl; // URL logo
    private Integer totalStaff; // Số nhân sự
    private String ownerName; // Tên chủ sở hữu
    private String status; // Trạng thái
    private String address; // Địa chỉ
}