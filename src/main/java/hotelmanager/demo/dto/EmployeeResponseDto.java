package hotelmanager.demo.dto;
import hotelmanager.demo.dto.auth.UserInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDto extends BaseDto{
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private Long birthDay;
    private String nationality;
    private String identityNumber;
    private String address;
    private String notes;
    private Long startDate;
    private String status;
    private String profilePictureUrl;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhone;
    private String positionName;
    private UserInfoDto user;
}
