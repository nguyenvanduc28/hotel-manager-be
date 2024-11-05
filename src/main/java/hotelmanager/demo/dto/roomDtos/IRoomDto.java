package hotelmanager.demo.dto.roomDtos;

public interface IRoomDto {
    Integer getId();
    String getRoomNumber();
    Integer getFloor();
    Integer getSize();
    Boolean getIsAvailable();
    Boolean getIsSmokingAllowed();
    Boolean getHasPrivateKitchen();
    Boolean getHasPrivateBathroom();
    Boolean getHasBalcony();
    Boolean getHasLakeView();
    Boolean getHasGardenView();
    Boolean getHasPoolView();
    Boolean getHasMountainView();
    Boolean getHasLandmarkView();
    Boolean getHasCityView();
    Boolean getHasRiverView();
    Boolean getHasCourtyardView();
    Boolean getHasFreeWifi();
    Boolean getHasSoundproofing();
    String getDescription();
    String getImages();
    Integer getRoomTypeId();
}
