package hotelmanager.demo.services;

import hotelmanager.demo.dto.HotelDto;
import hotelmanager.demo.dto.HotelSearchResultDto;
import hotelmanager.demo.dto.bookingDtos.ImageDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.models.Image;
import hotelmanager.demo.repositories.HotelRepository;
import hotelmanager.demo.repositories.ImageRepository;
import hotelmanager.demo.services.rooms.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private RoomService roomService;
    
    private ModelMapper modelMapper = new ModelMapper();

    public List<HotelSearchResultDto> searchHotels(String location, Long checkInDate, Long checkOutDate) {
        // Tìm các khách sạn theo location
        List<Hotel> hotels = hotelRepository.findByAddressOrCityContainingIgnoreCase(location);
        List<HotelSearchResultDto> results = new ArrayList<>();
        
        for (Hotel hotel : hotels) {
            // Kiểm tra phòng trống cho mỗi khách sạn
            List<RoomDto> availableRooms = roomService.getAvailableRooms(
                checkInDate, 
                checkOutDate, 
                hotel.getId()
            );

            if (!availableRooms.isEmpty()) {
                HotelSearchResultDto resultDto = modelMapper.map(hotel, HotelSearchResultDto.class);
                resultDto.setAvailableRooms(availableRooms);

                List<Image> images = imageRepository.findAllImagesByHotelId(hotel.getId());
                resultDto.setImages(List.of(modelMapper.map(images, ImageDto[].class)));

                // Tìm giá phòng thấp nhất
                double lowestPrice = availableRooms.stream()
                    .mapToDouble(room -> room.getRoomType().getBasePricePerNight())
                    .min()
                    .orElse(0.0);
                resultDto.setLowestPrice(lowestPrice);
                results.add(resultDto);
            }
        }
        
        return results;
    }
} 