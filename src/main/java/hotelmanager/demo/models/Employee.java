package hotelmanager.demo.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee extends BaseEntity{
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
    private Integer userId;
}
