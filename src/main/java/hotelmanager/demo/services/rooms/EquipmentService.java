package hotelmanager.demo.services.rooms;

import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Equipment;
import hotelmanager.demo.models.EquipmentCategory;
import hotelmanager.demo.repositories.EquipmentCategoryRepository;
import hotelmanager.demo.repositories.EquipmentRepository;
import hotelmanager.demo.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public EquipmentCategoryDto createEquipmentCategory(EquipmentCategoryDto equipmentCategoryDto, Integer hotelId) {
        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setName(equipmentCategoryDto.getName());
        equipmentCategory.setDescription(equipmentCategoryDto.getDescription());
        equipmentCategory.setHotelId(hotelId);
        EquipmentCategoryDto categoryDto = modelMapper.map(equipmentCategoryRepository.save(equipmentCategory), EquipmentCategoryDto.class);
        return categoryDto;
    }

    @Transactional
    public EquipmentDto createEquipment(EquipmentDto equipmentDto, Integer hotelId) {
        Equipment equipment = new Equipment();
        equipment.setName(equipmentDto.getName());
        equipment.setDescription(equipmentDto.getDescription());
        equipment.setBarcode(equipmentDto.getBarcode());
        equipment.setInstallationDate(equipmentDto.getInstallationDate());
        equipment.setStatus(equipmentDto.getStatus());
        equipment.setHotelId(hotelId);

        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentDto.getEquipmentCategory().getId())
                .orElseThrow(() -> new NotFoundException("not found category"));
        equipment.setEquipmentCategory(equipmentCategory);

        EquipmentDto equipmentDtoN = modelMapper.map(equipmentRepository.save(equipment), EquipmentDto.class);
        return equipmentDtoN;
    }
    @Transactional
    public List<EquipmentDto> createEquipmentList(List<EquipmentDto> equipmentDtos, Integer hotelId) {
        List<EquipmentDto> equipmentDtos1 = new ArrayList<>();
        for (EquipmentDto equipmentDto: equipmentDtos){
            Equipment equipment = new Equipment();
            equipment.setName(equipmentDto.getName());
            equipment.setDescription(equipmentDto.getDescription());
            equipment.setBarcode(equipmentDto.getBarcode());
            equipment.setInstallationDate(equipmentDto.getInstallationDate());
            equipment.setStatus(equipmentDto.getStatus());
            equipment.setHotelId(hotelId);
            EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentDto.getEquipmentCategory().getId())
                    .orElseThrow(() -> new NotFoundException("not found category"));
            equipment.setEquipmentCategory(equipmentCategory);

            EquipmentDto equipmentDtoN = modelMapper.map(equipmentRepository.save(equipment), EquipmentDto.class);
            equipmentDtos1.add(equipmentDtoN);
        }

        return equipmentDtos1;
    }
    @Transactional
    public EquipmentDto updateEquipment(EquipmentDto equipmentDto, Integer hotelId) {
        Equipment equipment = equipmentRepository.findById(equipmentDto.getId()).orElseThrow(()-> new NotFoundException("Không tìm thấy equipment"));
        equipment.setName(equipmentDto.getName());
        equipment.setDescription(equipmentDto.getDescription());
        equipment.setBarcode(equipmentDto.getBarcode());
        equipment.setInstallationDate(equipmentDto.getInstallationDate());
        equipment.setStatus(equipmentDto.getStatus());
        equipment.setHotelId(hotelId);
        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentDto.getEquipmentCategory().getId())
                .orElseThrow(() -> new NotFoundException("not found category"));
        equipment.setEquipmentCategory(equipmentCategory);

        EquipmentDto equipmentDtoN = modelMapper.map(equipmentRepository.save(equipment), EquipmentDto.class);
        return equipmentDtoN;
    }


    @Transactional(readOnly = true)
    public List<EquipmentCategoryDto> getAllEquipmentCategories(Integer hotelId) {
        return List.of(modelMapper.map(equipmentCategoryRepository.findAllEquipCateByHotelId(hotelId), EquipmentCategoryDto[].class));
    }

    @Transactional(readOnly = true)
    public List<EquipmentDto> getAllEquipment(Integer hotelId) {
        List<IEquipmentDto> equipments = equipmentRepository.findAllEquipment(hotelId);
        List<EquipmentDto> equipmentDtos1 = new ArrayList<>();
        for (IEquipmentDto iEquipmentDto : equipments) {
            EquipmentDto equipmentDto = modelMapper.map(iEquipmentDto, EquipmentDto.class);
            if (iEquipmentDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iEquipmentDto.getRoomId()), RoomDto.class);
                equipmentDto.setRoom(roomDto);
            }
            if (iEquipmentDto.getEquipmentCategoryId() != null) {
                EquipmentCategoryDto equipmentCategoryDto = modelMapper.map(equipmentCategoryRepository.findCategoryById(iEquipmentDto.getEquipmentCategoryId()), EquipmentCategoryDto.class);
                equipmentDto.setEquipmentCategory(equipmentCategoryDto);
            }
            equipmentDtos1.add(equipmentDto);
        }
        return equipmentDtos1;
    }
    @Transactional(readOnly = true)
    public List<EquipmentDto> getAllEquipmentAvailable(Integer roomId) {
        List<IEquipmentDto> equipments = equipmentRepository.findAllEquipmentAvalable(roomId);
        List<EquipmentDto> equipmentDtos1 = new ArrayList<>();
        for (IEquipmentDto iEquipmentDto : equipments) {
            EquipmentDto equipmentDto = modelMapper.map(iEquipmentDto, EquipmentDto.class);
            if (iEquipmentDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iEquipmentDto.getRoomId()), RoomDto.class);
                equipmentDto.setRoom(roomDto);
            }
            if (iEquipmentDto.getEquipmentCategoryId() != null) {
                EquipmentCategoryDto equipmentCategoryDto = modelMapper.map(equipmentCategoryRepository.findCategoryById(iEquipmentDto.getEquipmentCategoryId()), EquipmentCategoryDto.class);
                equipmentDto.setEquipmentCategory(equipmentCategoryDto);
            }
            equipmentDtos1.add(equipmentDto);
        }
        return equipmentDtos1;
    }

    @Transactional(readOnly = true) 
    public List<EquipmentDto> getAllEquipmentByRoomId(Integer roomId) {
        List<IEquipmentDto> equipments = equipmentRepository.findAllEquipmentByRoomId(roomId);
        List<EquipmentDto> equipmentDtos1 = new ArrayList<>();
        for (IEquipmentDto iEquipmentDto : equipments) {
            EquipmentDto equipmentDto = modelMapper.map(iEquipmentDto, EquipmentDto.class);
            if (iEquipmentDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iEquipmentDto.getRoomId()), RoomDto.class);
                equipmentDto.setRoom(roomDto);
            }
            if (iEquipmentDto.getEquipmentCategoryId() != null) {
                EquipmentCategoryDto equipmentCategoryDto = modelMapper.map(equipmentCategoryRepository.findCategoryById(iEquipmentDto.getEquipmentCategoryId()), EquipmentCategoryDto.class);
                equipmentDto.setEquipmentCategory(equipmentCategoryDto);
            }
            equipmentDtos1.add(equipmentDto);
        }
        return equipmentDtos1;
    }

    @Transactional(readOnly = true)
    public EquipmentDto getEquipmentById(Integer id) {
        IEquipmentDto iequipment = equipmentRepository.findEquipmentById(id);
        EquipmentDto equipmentDto = modelMapper.map(iequipment, EquipmentDto.class);
        if (iequipment.getRoomId() != null) {
            RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iequipment.getRoomId()), RoomDto.class);
            equipmentDto.setRoom(roomDto);
        }
        if (iequipment.getEquipmentCategoryId() != null) {
            EquipmentCategoryDto equipmentCategoryDto = modelMapper.map(equipmentCategoryRepository.findCategoryById(iequipment.getEquipmentCategoryId()), EquipmentCategoryDto.class);
            equipmentDto.setEquipmentCategory(equipmentCategoryDto);
        }

        return equipmentDto;
    }

}
