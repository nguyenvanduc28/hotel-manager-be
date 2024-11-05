package hotelmanager.demo.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseObject {
    private int responseCode;
    private String message;
    private Object data;

}
