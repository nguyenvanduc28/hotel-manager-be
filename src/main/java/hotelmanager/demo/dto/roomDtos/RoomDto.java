package hotelmanager.demo.dto.roomDtos;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.bookingDtos.ImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RoomDto extends BaseDto {
    private String roomNumber;
    private Integer floor;
    private Integer size;
    private Boolean isAvailable;
    private Boolean isSmokingAllowed;
    private Boolean hasPrivateKitchen;
    private Boolean hasPrivateBathroom;
    private Boolean hasBalcony;
    private Boolean hasLakeView;
    private Boolean hasGardenView;
    private Boolean hasPoolView;
    private Boolean hasMountainView;
    private Boolean hasLandmarkView;
    private Boolean hasCityView;
    private Boolean hasRiverView;
    private Boolean hasCourtyardView;
    private Boolean hasFreeWifi;
    private Boolean hasSoundproofing;
    private String description;
    private String images;
    private RoomTypeDto roomType;
    private List<ConsumableDto> consumables;
    private List<EquipmentDto> equipmentList;
    private List<ImageDto> imageList;
}
