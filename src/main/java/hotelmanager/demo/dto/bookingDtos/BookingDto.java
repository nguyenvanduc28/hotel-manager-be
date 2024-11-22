package hotelmanager.demo.dto.bookingDtos;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingDto extends BaseDto {
    private CustomerDto customer;
    private Long checkInDate;
    private Long checkInTime;
    private Long checkOutTime;
    private Long checkOutDate;
    private Long confirmedTime;
    private Long estimatedArrivalTime;
    private Long bookingDate;
    private Boolean isGroup;
    private Double totalCost; //tiền đặt phòng
    private String status;
    private Double deposit; //tiền cọc
    private String cancellationPolicy;
    private Long canceledAt;
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private Boolean isGuaranteed;
    private List<RoomDto> rooms;
    private List<BookingConsumableDto> consumablesUsed;
    private List<BookingEquipmentDamagedDto> equipmentDamagedList;
    private List<ImageDto> images;
    
}
