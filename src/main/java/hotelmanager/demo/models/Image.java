package hotelmanager.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
public class Image extends BaseEntity {
    private String url;
    private String type;
    private Long size;
    private Integer roomId;
    private Integer hotelId;
    private String publicId;
    private String fileName;
    private String description;
}
