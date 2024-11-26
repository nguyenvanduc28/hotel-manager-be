
package hotelmanager.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceCountDto {
    private long numOfNewOrder;
    private long numOfInProgressOrder;
    private long numOfReadyToServeOrder;
}
