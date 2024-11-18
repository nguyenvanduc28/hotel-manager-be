package hotelmanager.demo.services;

import hotelmanager.demo.dto.HotelSearchResultDto;
import hotelmanager.demo.dto.INumOfHotelSearch;
import hotelmanager.demo.dto.bookingDtos.ImageDto;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.models.Image;
import hotelmanager.demo.repositories.HotelRepository;
import hotelmanager.demo.repositories.ImageRepository;
import hotelmanager.demo.repositories.RoomRepository;
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
    private RoomRepository roomRepository;
    
    @Autowired
    private RoomService roomService;
    
    private ModelMapper modelMapper = new ModelMapper();

    public List<HotelSearchResultDto> searchHotels(String location, Long checkInDate, Long checkOutDate) {
        List<Hotel> hotels = hotelRepository.findByAddressOrCityContainingIgnoreCase(location);
        List<HotelSearchResultDto> results = new ArrayList<>();
        
        for (Hotel hotel : hotels) {
            INumOfHotelSearch availableRoomCount = roomRepository.getAvailableRoomCount(
                    checkInDate,
                    checkOutDate,
                    hotel.getId()
            );

            if (availableRoomCount.getCount() > 0) {
                HotelSearchResultDto resultDto = modelMapper.map(hotel, HotelSearchResultDto.class);
                resultDto.setAvailableRoomCount(availableRoomCount.getCount());

                List<Image> images = imageRepository.findAllImagesByHotelId(hotel.getId());
                resultDto.setImages(List.of(modelMapper.map(images, ImageDto[].class)));

                resultDto.setLowestPrice(availableRoomCount.getLowestPrice());
                
                results.add(resultDto);
            }
        }
        
        return results;
    }
} 