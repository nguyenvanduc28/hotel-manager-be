package hotelmanager.demo.dto;

import hotelmanager.demo.dto.bookingDtos.ImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class HotelSearchResultDto {
    private Integer id;
    private String name;
    private String address;
    private String description;
    private Integer rating;
    private List<ImageDto> images;
    private int availableRoomCount;
    private double lowestPrice;
    private Boolean hasWifi;
    private Boolean hasParking;
    private Boolean hasRestaurant; 
    private Boolean hasSwimmingPool;
    private Boolean hasConferenceRoom;
    private Boolean has24HourFrontDesk;
    private Boolean hasBar;
    private Boolean hasElevator;
    private Boolean hasAirConditioning;
    private Boolean hasShuttle;
    private String otherAmenities;

    private Double longitude;
    private Double latitude;

    private String logoUrl;
} 
