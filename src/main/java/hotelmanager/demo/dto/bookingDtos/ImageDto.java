package hotelmanager.demo.dto.bookingDtos;

import hotelmanager.demo.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto extends BaseDto {
    private String url;
    private String type;
    private Long size;
    private Integer roomId;
    private Integer hotelId;
    private String publicId;
    private String fileName;
    private String description;
}
