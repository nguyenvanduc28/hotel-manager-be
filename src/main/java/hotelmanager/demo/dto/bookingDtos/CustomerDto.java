package hotelmanager.demo.dto.bookingDtos;


import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto extends BaseDto {
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private Long birthDay;
    private String nationality;
    private String identityNumber;
    private String address;
    private String notes;
}