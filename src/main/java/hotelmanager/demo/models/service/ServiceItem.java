package hotelmanager.demo.models.service;

import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ServiceItem extends BaseEntity {
    private String name;
    private Long price;
    private String image;
    private String description;
    private Integer serviceTypeId;

}
