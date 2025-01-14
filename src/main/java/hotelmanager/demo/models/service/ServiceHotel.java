package hotelmanager.demo.models.service;

import hotelmanager.demo.models.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class ServiceHotel extends BaseEntity {
    private String name;
    private String serviceType;
    private String description;
}
