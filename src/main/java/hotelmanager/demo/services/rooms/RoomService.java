package hotelmanager.demo.services.rooms;

import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.*;
import hotelmanager.demo.repositories.ConsumableRepository;
import hotelmanager.demo.repositories.EquipmentRepository;
import hotelmanager.demo.repositories.RoomRepository;
import hotelmanager.demo.repositories.RoomTypeRepository;
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

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public RoomTypeDto createRoomType (RoomTypeDto roomTypeDto) {
        RoomType roomType = new RoomType();
        roomType.setName(roomTypeDto.getName());
        roomType.setDescription(roomTypeDto.getDescription());
        roomType.setBasePricePerNight(roomTypeDto.getBasePricePerNight());
        roomType.setMaxOccupancy(roomTypeDto.getMaxOccupancy());
        roomType.setDoubleBedCount(roomTypeDto.getDoubleBedCount());
        roomType.setExtraBedAvailable(roomTypeDto.getExtraBedAvailable());
        roomType.setSizeRange(roomTypeDto.getSizeRange());
        roomType.setSingleBedCount(roomTypeDto.getSingleBedCount());

        RoomTypeDto roomTypeDto1 = modelMapper.map(roomTypeRepository.save(roomType), RoomTypeDto.class);
        return roomTypeDto1;
    }
    @Transactional
    public RoomDto createRoom (RoomDto roomDto) {
        Room room = new Room();
        room.setRoomNumber(roomDto.getRoomNumber());
        RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId())
                        .orElseThrow(() -> new NotFoundException("not found room type"));

        room.setRoomType(roomType);
        room.setDescription(roomDto.getDescription());
        room.setFloor(roomDto.getFloor());
        room.setHasBalcony(roomDto.getHasBalcony());
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
        Room roomNew = roomRepository.save(room);

        for (ConsumableDto consumableDto : roomDto.getConsumables()) {
            Consumable consumable = consumableRepository.findById(consumableDto.getId())
                    .orElseThrow(() -> new NotFoundException("not found consumable"));
            consumableRepository.updateRoomId(consumable.getId(), roomNew.getId());
        }
        // Cập nhật danh sách equipment
        for (EquipmentDto equipmentDto : roomDto.getEquipmentList()) {
            Equipment equipment = equipmentRepository.findById(equipmentDto.getId())
                    .orElseThrow(() -> new NotFoundException("not found equipment"));
            equipment.setRoom(roomNew);
            equipmentRepository.updateRoomId(equipment.getId(), roomNew.getId());
        }

        return modelMapper.map(roomNew, RoomDto.class);
    }
    @Transactional
    public RoomDto updateRoom (RoomDto roomDto) {
        IRoomDto roomDto1 = roomRepository.findRoomById(roomDto.getId());
        if (roomDto1 == null) throw new NotFoundException("Không tìm thấy room");
        Room room = modelMapper.map(roomDto1, Room.class);

        room.setRoomNumber(roomDto.getRoomNumber());
        RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId())
                .orElseThrow(() -> new NotFoundException("not found room type"));

        room.setRoomType(roomType);
        room.setDescription(roomDto.getDescription());
        room.setFloor(roomDto.getFloor());
        room.setSize(roomDto.getSize());
        room.setHasBalcony(roomDto.getHasBalcony());
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
        Room roomNew = roomRepository.save(room);

        //danh sách consumables cũ.
        List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
        for (IConsumableDto consumableDto: consumableDtos) {
            consumableRepository.updateRoomId(consumableDto.getId(), null);
        }
        //cập nhật danh sách mới.
        for (ConsumableDto consumableDto : roomDto.getConsumables()) {
            consumableRepository.updateRoomId(consumableDto.getId(), roomNew.getId());
        }

        //danh sách equipment cũ
        List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
        for (IEquipmentDto equipmentDto: equipmentDtos) {
            equipmentRepository.updateRoomId(equipmentDto.getId(), null);
        }
        //cập nhật danh sách mới.
        for (EquipmentDto equipmentDto : roomDto.getEquipmentList()) {
            equipmentRepository.updateRoomId(equipmentDto.getId(), roomNew.getId());
        }

        return modelMapper.map(roomNew, RoomDto.class);
    }
    @Transactional(readOnly = true)
    public List<RoomTypeDto> getAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

        for (RoomType roomType : roomTypes) {
            RoomTypeDto roomTypeDto = modelMapper.map(roomType, RoomTypeDto.class);
            roomTypeDtos.add(roomTypeDto);
        }

        return roomTypeDtos;
    }
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Integer id) {
        IRoomDto iRoomDto = roomRepository.findRoomById(id);
        if (iRoomDto == null) throw new NotFoundException("Không tìm thấy room");
        RoomDto roomDto = modelMapper.map(iRoomDto, RoomDto.class);

        RoomType roomType = roomTypeRepository.findById(iRoomDto.getRoomTypeId()).orElseThrow(() -> new NotFoundException("Lỗi type room"));
        roomDto.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));
        List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
        List<ConsumableDto> consumableDtos1 = List.of(modelMapper.map(consumableDtos, ConsumableDto[].class));
        roomDto.setConsumables(consumableDtos1);

        List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
        List<EquipmentDto> equipmentDtos1 = List.of(modelMapper.map(equipmentDtos, EquipmentDto[].class));
        roomDto.setEquipmentList(equipmentDtos1);

        return roomDto;
    }
    @Transactional(readOnly = true)
    public List<RoomDto> getAvailableRooms(Long checkInDate, Long checkOutDate) {
        List<IRoomDto> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (IRoomDto iRoomDto : availableRooms) {
            RoomDto roomDto = modelMapper.map(iRoomDto, RoomDto.class);
            RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(()->new NotFoundException("Lỗi"));
            roomDto.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));
            //lấy tiện ích
            List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
            List<ConsumableDto> consumableDtos1 = List.of(modelMapper.map(consumableDtos, ConsumableDto[].class));

            List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
            List<EquipmentDto> equipmentDtos1 = List.of(modelMapper.map(equipmentDtos, EquipmentDto[].class));
            roomDto.setConsumables(consumableDtos1);
            roomDto.setEquipmentList(equipmentDtos1);
            roomDtos.add(roomDto);
        }

        return roomDtos;
    }
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        List<IRoomDto> iRoomDtos = roomRepository.findAllRooms();
        List<RoomDto> roomDtos = new ArrayList<>();
        for (IRoomDto iRoomDto : iRoomDtos) {
            RoomDto roomDto = modelMapper.map(iRoomDto, RoomDto.class);
            RoomType roomType = roomTypeRepository.findById(roomDto.getRoomType().getId()).orElseThrow(()->new NotFoundException("Lỗi"));
            roomDto.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));

            List<IConsumableDto> consumableDtos = consumableRepository.findConsumablesByRoomId(roomDto.getId());
            List<ConsumableDto> consumableDtos1 = List.of(modelMapper.map(consumableDtos, ConsumableDto[].class));

            List<IEquipmentDto> equipmentDtos = equipmentRepository.findEquipmentByRoomId(roomDto.getId());
            List<EquipmentDto> equipmentDtos1 = List.of(modelMapper.map(equipmentDtos, EquipmentDto[].class));
            roomDto.setConsumables(consumableDtos1);
            roomDto.setEquipmentList(equipmentDtos1);
            roomDtos.add(roomDto);
        }

        return roomDtos;
    }

}
