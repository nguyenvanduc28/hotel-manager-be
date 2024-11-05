package hotelmanager.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends BaseEntity {

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
