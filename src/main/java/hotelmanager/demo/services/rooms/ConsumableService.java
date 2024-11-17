package hotelmanager.demo.services.rooms;

import hotelmanager.demo.dto.roomDtos.ConsumableCategoryDto;
import hotelmanager.demo.dto.roomDtos.ConsumableDto;
import hotelmanager.demo.dto.roomDtos.IConsumableDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Consumable;
import hotelmanager.demo.models.ConsumableCategory;
import hotelmanager.demo.models.Room;
import hotelmanager.demo.repositories.ConsumableCategoryRepository;
import hotelmanager.demo.repositories.ConsumableRepository;
import hotelmanager.demo.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsumableService {
    @Autowired
    private ConsumableRepository consumableRepository;
    @Autowired
    private ConsumableCategoryRepository consumableCategoryRepository;
    @Autowired
    private RoomRepository roomRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public ConsumableCategoryDto createConsumableCategory(ConsumableCategoryDto consumableCategoryDto, Integer hotelId) {
        ConsumableCategory consumableCategory = new ConsumableCategory();
        consumableCategory.setName(consumableCategoryDto.getName());
        consumableCategory.setDescription(consumableCategoryDto.getDescription());
        consumableCategory.setHotelId(hotelId);
        ConsumableCategoryDto categoryDto = modelMapper.map(consumableCategoryRepository.save(consumableCategory), ConsumableCategoryDto.class);
        return categoryDto;
    }

    @Transactional
    public ConsumableDto createConsumable(ConsumableDto consumableDto, Integer hotelId) {
        Consumable consumable = new Consumable();
        consumable.setName(consumableDto.getName());
        consumable.setDescription(consumableDto.getDescription());
        consumable.setPrice(consumableDto.getPrice());
        consumable.setQuantity(consumableDto.getQuantity());
        consumable.setBarcode(consumableDto.getBarcode());
        consumable.setUnit(consumableDto.getUnit());
        consumable.setExpiryDate(consumableDto.getExpiryDate());
        consumable.setHotelId(hotelId);
        ConsumableCategory consumableCategory = consumableCategoryRepository.findById(consumableDto.getConsumableCategory().getId())
                .orElseThrow(() -> new NotFoundException("not found category"));
        consumable.setConsumableCategory(consumableCategory);


        ConsumableDto consumableDtoN = modelMapper.map(consumableRepository.save(consumable), ConsumableDto.class);
        return consumableDtoN;
    }
    @Transactional
    public List<ConsumableDto> createConsumableList(List<ConsumableDto> consumableDtos, Integer hotelId) {
        List<ConsumableDto> consumableDtos1 = new ArrayList<>();

        for (ConsumableDto consumableDto:consumableDtos) {
        Consumable consumable = new Consumable();
        consumable.setName(consumableDto.getName());
        consumable.setDescription(consumableDto.getDescription());
        consumable.setPrice(consumableDto.getPrice());
        consumable.setQuantity(consumableDto.getQuantity());
        consumable.setBarcode(consumableDto.getBarcode());
        consumable.setUnit(consumableDto.getUnit());
        consumable.setExpiryDate(consumableDto.getExpiryDate());
        consumable.setHotelId(hotelId);
        ConsumableCategory consumableCategory = consumableCategoryRepository.findById(consumableDto.getConsumableCategory().getId())
                .orElseThrow(() -> new NotFoundException("not found category"));
        consumable.setConsumableCategory(consumableCategory);


        ConsumableDto consumableDtoN = modelMapper.map(consumableRepository.save(consumable), ConsumableDto.class);
        consumableDtos1.add(consumableDtoN);
        }

        return consumableDtos1;
    }
    @Transactional
    public ConsumableDto updateConsumable(ConsumableDto consumableDto, Integer hotelId) {
        Consumable consumable = consumableRepository.findById(consumableDto.getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy consumable"));
        consumable.setName(consumableDto.getName());
        consumable.setDescription(consumableDto.getDescription());
        consumable.setPrice(consumableDto.getPrice());
        consumable.setQuantity(consumableDto.getQuantity());
        consumable.setBarcode(consumableDto.getBarcode());
        consumable.setUnit(consumableDto.getUnit());
        consumable.setExpiryDate(consumableDto.getExpiryDate());
        consumable.setHotelId(hotelId);
        ConsumableCategory consumableCategory = consumableCategoryRepository.findById(consumableDto.getConsumableCategory().getId())
                .orElseThrow(() -> new NotFoundException("not found category"));
        consumable.setConsumableCategory(consumableCategory);


        ConsumableDto consumableDtoN = modelMapper.map(consumableRepository.save(consumable), ConsumableDto.class);
        return consumableDtoN;
    }
    @Transactional(readOnly = true)
    public List<ConsumableCategoryDto> getAllConsumableCategories(Integer hotelId) {
        return List.of(modelMapper.map(consumableCategoryRepository.findAllConCategoriesByHotelId(hotelId), ConsumableCategoryDto[].class));
    }

    @Transactional(readOnly = true)
    public List<ConsumableDto> getAllConsumables(Integer hotelId) {
        List<IConsumableDto> consumableDtos = consumableRepository.findAllConsumables(hotelId);
        List<ConsumableDto> consumableDtos2 = new ArrayList<>();
        for (IConsumableDto iConsumableDto:consumableDtos) {
            ConsumableDto consumableDto = modelMapper.map(iConsumableDto, ConsumableDto.class);

            if (iConsumableDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iConsumableDto.getRoomId()), RoomDto.class);
                consumableDto.setRoom(roomDto);
            }
            if (iConsumableDto.getConsumableCategoryId() != null) {
                ConsumableCategoryDto categoryDto = modelMapper.map(consumableCategoryRepository.findCategoryById(iConsumableDto.getConsumableCategoryId()), ConsumableCategoryDto.class);
                consumableDto.setConsumableCategory(categoryDto);
            }
            consumableDtos2.add(consumableDto);
        }
        return consumableDtos2;
    }

    @Transactional(readOnly = true)
    public List<ConsumableDto> getAllConsumablesAvailable(Integer roomId, Integer hotelId) {
        List<IConsumableDto> consumableDtos = consumableRepository.findAllConsumablesAvailable(roomId, hotelId);
        List<ConsumableDto> consumableDtos2 = new ArrayList<>();
        for (IConsumableDto iConsumableDto:consumableDtos) {
            ConsumableDto consumableDto = modelMapper.map(iConsumableDto, ConsumableDto.class);

            if (iConsumableDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iConsumableDto.getRoomId()), RoomDto.class);
                consumableDto.setRoom(roomDto);
            }
            if (iConsumableDto.getConsumableCategoryId() != null) {
                ConsumableCategoryDto categoryDto = modelMapper.map(consumableCategoryRepository.findCategoryById(iConsumableDto.getConsumableCategoryId()), ConsumableCategoryDto.class);
                consumableDto.setConsumableCategory(categoryDto);
            }
            consumableDtos2.add(consumableDto);
        }
        return consumableDtos2;
    }

    @Transactional(readOnly = true)
    public List<ConsumableDto> getAllConsumablesByRoomId(Integer roomId) {
        List<IConsumableDto> consumableDtos = consumableRepository.findAllConsumablesByRoomId(roomId);
        List<ConsumableDto> consumableDtos2 = new ArrayList<>();
        for (IConsumableDto iConsumableDto:consumableDtos) {
            ConsumableDto consumableDto = modelMapper.map(iConsumableDto, ConsumableDto.class);
            if (iConsumableDto.getRoomId() != null) {
                RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iConsumableDto.getRoomId()), RoomDto.class);
                consumableDto.setRoom(roomDto);
            }
            if (iConsumableDto.getConsumableCategoryId() != null) {
                ConsumableCategoryDto categoryDto = modelMapper.map(consumableCategoryRepository.findCategoryById(iConsumableDto.getConsumableCategoryId()), ConsumableCategoryDto.class);
                consumableDto.setConsumableCategory(categoryDto);
            }
            consumableDtos2.add(consumableDto);
        }
        return consumableDtos2;
    }


    @Transactional(readOnly = true)
    public ConsumableDto getConsumableById(Integer id) {
        IConsumableDto iConsumableDto = consumableRepository.findConsumableById(id);
        ConsumableDto consumableDtos2 = modelMapper.map(iConsumableDto, ConsumableDto.class);

        if (iConsumableDto.getRoomId() != null) {
            RoomDto roomDto = modelMapper.map(roomRepository.findRoomById(iConsumableDto.getRoomId()), RoomDto.class);
            consumableDtos2.setRoom(roomDto);
        }
        if (iConsumableDto.getConsumableCategoryId() != null) {
            ConsumableCategoryDto categoryDto = modelMapper.map(consumableCategoryRepository.findCategoryById(iConsumableDto.getConsumableCategoryId()), ConsumableCategoryDto.class);
            consumableDtos2.setConsumableCategory(categoryDto);
        }
        return consumableDtos2;
    }


}
