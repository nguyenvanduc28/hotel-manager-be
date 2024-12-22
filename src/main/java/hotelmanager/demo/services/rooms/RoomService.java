package hotelmanager.demo.services.rooms;

import hotelmanager.demo.dto.bookingDtos.ImageDto;
import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.*;
import hotelmanager.demo.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ConsumableRepository consumableRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private RoomTypePriceRepository roomTypePriceRepository;
    private ModelMapper modelMapper = new ModelMapper();

    // Helper method to map room properties
    private void mapRoomProperties(Room room, RoomDto roomDto) {
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setDescription(roomDto.getDescription());
        room.setFloor(roomDto.getFloor());
        room.setSize(roomDto.getSize());
        room.setIsAvailable(roomDto.getIsAvailable());
        room.setIsSmokingAllowed(roomDto.getIsSmokingAllowed());
        room.setHasPrivateKitchen(roomDto.getHasPrivateKitchen());
        room.setHasPrivateBathroom(roomDto.getHasPrivateBathroom());
        room.setHasBalcony(roomDto.getHasBalcony());
        room.setHasLakeView(roomDto.getHasLakeView());
        room.setHasGardenView(roomDto.getHasGardenView());
        room.setHasPoolView(roomDto.getHasPoolView());
        room.setHasMountainView(roomDto.getHasMountainView());
        room.setHasLandmarkView(roomDto.getHasLandmarkView());
        room.setHasCityView(roomDto.getHasCityView());
        room.setHasRiverView(roomDto.getHasRiverView());
        room.setHasCourtyardView(roomDto.getHasCourtyardView());
        room.setHasFreeWifi(roomDto.getHasFreeWifi());
        room.setHasSoundproofing(roomDto.getHasSoundproofing());
    }

    // Helper method to update room relationships
    private void updateRoomRelationships(Room room, RoomDto roomDto) {
        // Update consumables
        consumableRepository.findConsumablesByRoomId(room.getId())
            .forEach(c -> consumableRepository.updateRoomId(c.getId(), null));
        roomDto.getConsumables()
            .forEach(c -> consumableRepository.updateRoomId(c.getId(), room.getId()));

        // Update equipment
        equipmentRepository.findEquipmentByRoomId(room.getId())
            .forEach(e -> equipmentRepository.updateRoomId(e.getId(), null));
        roomDto.getEquipmentList()
            .forEach(e -> equipmentRepository.updateRoomId(e.getId(), room.getId()));

        // Update images
        imageRepository.findAllImagesByRoomId(room.getId())
            .forEach(i -> imageRepository.deleteRoomIdByImageId(i.getId()));
        roomDto.getImageList()
            .forEach(i -> imageRepository.updateRoomIdByImageId(room.getId(), i.getId()));
    }

    // Helper method to populate room DTOs
    private RoomDto populateRoomDto(IRoomDto iRoomDto) {
        RoomDto roomDto = modelMapper.map(iRoomDto, RoomDto.class);
        
        RoomType roomType = roomTypeRepository.findById(iRoomDto.getRoomTypeId())
            .orElseThrow(() -> new NotFoundException("Room type not found"));
        roomDto.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));

        List<ConsumableDto> consumables = List.of(modelMapper.map(
            consumableRepository.findConsumablesByRoomId(roomDto.getId()),
            ConsumableDto[].class));
        roomDto.setConsumables(consumables);

        List<EquipmentDto> equipment = List.of(modelMapper.map(
            equipmentRepository.findEquipmentByRoomId(roomDto.getId()),
            EquipmentDto[].class));
        roomDto.setEquipmentList(equipment);

        List<ImageDto> imageDtos = List.of(modelMapper.map(
            imageRepository.findAllImagesByRoomId(roomDto.getId()),
            ImageDto[].class));
        roomDto.setImageList(imageDtos);

        return roomDto;
    }

    @Transactional
    public RoomTypeDto createRoomType (RoomTypeDto roomTypeDto, Integer hotelId) {
        RoomType roomType = new RoomType();
        roomType.setName(roomTypeDto.getName());
        roomType.setDescription(roomTypeDto.getDescription());
        roomType.setBasePricePerNight(roomTypeDto.getBasePricePerNight());
        roomType.setMaxOccupancy(roomTypeDto.getMaxOccupancy());
        roomType.setDoubleBedCount(roomTypeDto.getDoubleBedCount());
        roomType.setExtraBedAvailable(roomTypeDto.getExtraBedAvailable());
        roomType.setSizeRange(roomTypeDto.getSizeRange());
        roomType.setSingleBedCount(roomTypeDto.getSingleBedCount());
        roomType.setHotelId(hotelId);
        RoomTypeDto roomTypeDto1 = modelMapper.map(roomTypeRepository.save(roomType), RoomTypeDto.class);
        return roomTypeDto1;
    }
    @Transactional
    public RoomDto createRoom (RoomDto roomDto, Integer hotelId) {
        Room room = new Room();
        mapRoomProperties(room, roomDto);
        room.setHotelId(hotelId);
        RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId())
            .orElseThrow(() -> new NotFoundException("Room type not found"));
        room.setRoomType(roomType);
        
        Room savedRoom = roomRepository.save(room);
        updateRoomRelationships(savedRoom, roomDto);
        
        return modelMapper.map(savedRoom, RoomDto.class);
    }
    @Transactional
    public RoomDto updateRoom (RoomDto roomDto, Integer hotelId) {
        IRoomDto roomDto1 = roomRepository.findRoomById(roomDto.getId());
        if (roomDto1 == null) throw new NotFoundException("Không tìm thấy room");
        Room room = modelMapper.map(roomDto1, Room.class);

        mapRoomProperties(room, roomDto);
        room.setHotelId(hotelId);
        RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId())
            .orElseThrow(() -> new NotFoundException("not found room type"));

        room.setRoomType(roomType);
        room.setHasBalcony(roomDto.getHasBalcony());
        room.setHasLakeView(roomDto.getHasLakeView());
        room.setHasGardenView(roomDto.getHasGardenView());
        room.setHasPoolView(roomDto.getHasPoolView());
        room.setHasMountainView(roomDto.getHasMountainView());
        room.setHasLandmarkView(roomDto.getHasLandmarkView());
        room.setHasCityView(roomDto.getHasCityView());
        room.setHasRiverView(roomDto.getHasRiverView());
        room.setHasCourtyardView(roomDto.getHasCourtyardView());
        room.setHasFreeWifi(roomDto.getHasFreeWifi());
        room.setHasSoundproofing(roomDto.getHasSoundproofing());
        Room roomNew = roomRepository.save(room);

        updateRoomRelationships(roomNew, roomDto);

        return modelMapper.map(roomNew, RoomDto.class);
    }
    @Transactional(readOnly = true)
    public List<RoomTypeDto> getAllRoomTypes(Integer hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findAllByHotelId(hotelId);
        List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

        for (RoomType roomType : roomTypes) {
            RoomTypeDto roomTypeDto = modelMapper.map(roomType, RoomTypeDto.class);
            List<RoomPrice> roomPrices = roomTypePriceRepository.findAllByRoomTypeId(roomType.getId());
            roomTypeDto.setRoomPrices(List.of(modelMapper.map(roomPrices, RoomPriceDto[].class)));
            roomTypeDtos.add(roomTypeDto);
        }

        return roomTypeDtos;
    }
    @Transactional(readOnly = true)
    public List<RoomTypeDto> getAllRoomTypesWithPriceInRange(Long checkInDate, Long checkOutDate, Integer hotelId) {
        List<RoomType> roomTypes = roomTypeRepository.findAllByHotelId(hotelId);
        List<RoomTypeDto> roomTypeDtos = new ArrayList<>();
        for (RoomType roomType : roomTypes) {
            RoomTypeDto roomTypeDto = modelMapper.map(roomType, RoomTypeDto.class);
            List<RoomPrice> roomPrices = roomTypePriceRepository.findAllRoomPricesInRange(roomType.getId(), checkInDate-1, checkOutDate+1);
            roomTypeDto.setRoomPrices(List.of(modelMapper.map(roomPrices, RoomPriceDto[].class)));
            roomTypeDtos.add(roomTypeDto);
        }
        return roomTypeDtos;
    }
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Integer id) {
        IRoomDto iRoomDto = roomRepository.findRoomById(id);
        if (iRoomDto == null) throw new NotFoundException("Không tìm thấy room");
        RoomDto roomDto = populateRoomDto(iRoomDto);

        return roomDto;
    }
    @Transactional(readOnly = true)
    public RoomDto getRoomByIdAndHotelId(Integer id, Integer hotelId) {
        IRoomDto iRoomDto = roomRepository.findRoomByIdAndHotelId(id, hotelId);
        if (iRoomDto == null) throw new NotFoundException("Không tìm thấy room");
        RoomDto roomDto = populateRoomDto(iRoomDto);

        return roomDto;
    }
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRooms(Long checkInDate, Long checkOutDate, Integer hotelId) {
        List<IRoomDto> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate, hotelId);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (IRoomDto iRoomDto : availableRooms) {
            RoomDto roomDto = populateRoomDto(iRoomDto);
            RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(()->new NotFoundException("Lỗi"));
            RoomTypeDto roomTypeDto = modelMapper.map(roomType, RoomTypeDto.class);
            RoomPrice roomPrice = roomTypePriceRepository.findByRoomTypeIdAndDate(roomType.getId(), checkInDate);
            if (roomPrice != null) roomTypeDto.setPriceToday(roomPrice.getPrice());
            roomDto.setRoomType(roomTypeDto);
            //lấy tiện ích
            List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
            List<ConsumableDto> consumableDtos1 = List.of(modelMapper.map(consumableDtos, ConsumableDto[].class));

            List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
            List<EquipmentDto> equipmentDtos1 = List.of(modelMapper.map(equipmentDtos, EquipmentDto[].class));

            List<ImageDto> imageDtos = List.of(modelMapper.map(
                imageRepository.findAllImagesByRoomId(roomDto.getId()),
                ImageDto[].class));

            roomDto.setConsumables(consumableDtos1);
            roomDto.setEquipmentList(equipmentDtos1);
            roomDto.setImageList(imageDtos);
            roomDtos.add(roomDto);
        }

        return roomDtos;
    }
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms(Integer hotelId) {
        List<IRoomDto> iRoomDtos = roomRepository.findAllRooms(hotelId);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (IRoomDto iRoomDto : iRoomDtos) {
            RoomDto roomDto = populateRoomDto(iRoomDto);
            RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(()->new NotFoundException("Lỗi"));
            roomDto.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));

            List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
            List<ConsumableDto> consumableDtos1 = List.of(modelMapper.map(consumableDtos, ConsumableDto[].class));

            List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
            List<EquipmentDto> equipmentDtos1 = List.of(modelMapper.map(equipmentDtos, EquipmentDto[].class));

            List<ImageDto> imageDtos = List.of(modelMapper.map(
                imageRepository.findAllImagesByRoomId(roomDto.getId()),
                ImageDto[].class));

            roomDto.setConsumables(consumableDtos1);
            roomDto.setEquipmentList(equipmentDtos1);
            roomDto.setImageList(imageDtos);
            roomDtos.add(roomDto);
        }

        return roomDtos;
    }

    @Transactional
    public RoomPriceDto updatePriceRoomType(RoomPriceDto roomPriceDto) {
        RoomPrice roomPrice = roomTypePriceRepository.findByRoomTypeIdAndDate(roomPriceDto.getRoomTypeId(), roomPriceDto.getDate());
        if (roomPrice == null) {
            roomPrice = new RoomPrice();
            roomPrice.setRoomTypeId(roomPriceDto.getRoomTypeId());
            roomPrice.setDate(roomPriceDto.getDate());
        }
        roomPrice.setPrice(roomPriceDto.getPrice());
        RoomPrice roomPrice1 = roomTypePriceRepository.save(roomPrice);
        return modelMapper.map(roomPrice1, RoomPriceDto.class);
    }

}
