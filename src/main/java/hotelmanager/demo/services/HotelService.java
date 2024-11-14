package hotelmanager.demo.services;

import hotelmanager.demo.dto.bookingDtos.ImageDto;
import hotelmanager.demo.models.Image;
import hotelmanager.demo.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import hotelmanager.demo.dto.HotelDto;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.repositories.ImageRepository;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ImageRepository imageRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public HotelDto createHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDto.getName());
        hotel.setRating(hotelDto.getRating());
        hotel.setCity(hotelDto.getCity());
        hotel.setPostalCode(hotelDto.getPostalCode());
        hotel.setCountry(hotelDto.getCountry());
        hotel.setLatitude(hotelDto.getLatitude());
        hotel.setLongitude(hotelDto.getLongitude());
        hotel.setPhoneNumber(hotelDto.getPhoneNumber());
        hotel.setEmail(hotelDto.getEmail());
        hotel.setWebsiteUrl(hotelDto.getWebsiteUrl());
        hotel.setNumberOfRooms(hotelDto.getNumberOfRooms());
        hotel.setCheckInTime(hotelDto.getCheckInTime());
        hotel.setCheckOutTime(hotelDto.getCheckOutTime());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setLogoUrl(hotelDto.getLogoUrl());
        hotel.setTotalStaff(hotelDto.getTotalStaff());
        hotel.setOwnerName(hotelDto.getOwnerName());
        hotel.setStatus(hotelDto.getStatus());
        hotel.setAddress(hotelDto.getAddress());

        Hotel savedHotel = hotelRepository.save(hotel);
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Transactional
    public HotelDto updateHotel(Integer id, HotelDto hotelDto) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn với ID: " + id));

        hotel.setName(hotelDto.getName());
        hotel.setRating(hotelDto.getRating());
        hotel.setCity(hotelDto.getCity());
        hotel.setPostalCode(hotelDto.getPostalCode());
        hotel.setCountry(hotelDto.getCountry()); 
        hotel.setLatitude(hotelDto.getLatitude());
        hotel.setLongitude(hotelDto.getLongitude());
        hotel.setPhoneNumber(hotelDto.getPhoneNumber());
        hotel.setEmail(hotelDto.getEmail());
        hotel.setWebsiteUrl(hotelDto.getWebsiteUrl());
        hotel.setNumberOfRooms(hotelDto.getNumberOfRooms());
        hotel.setCheckInTime(hotelDto.getCheckInTime());
        hotel.setCheckOutTime(hotelDto.getCheckOutTime());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setLogoUrl(hotelDto.getLogoUrl());
        hotel.setTotalStaff(hotelDto.getTotalStaff());
        hotel.setOwnerName(hotelDto.getOwnerName());
        hotel.setStatus(hotelDto.getStatus());
        hotel.setAddress(hotelDto.getAddress());

        // Update images
        imageRepository.findAllImagesByHotelId(hotel.getId())
            .forEach(i -> imageRepository.deleteHotelIdByImageId(i.getId()));
        if (hotelDto.getImages() != null) {
            hotelDto.getImages()
                .forEach(i -> imageRepository.updateHotelIdByImageId(hotel.getId(), i.getId()));
        }

        Hotel updatedHotel = hotelRepository.save(hotel);
        return modelMapper.map(updatedHotel, HotelDto.class);
    }

    public HotelDto getHotelById(Integer id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn với ID: " + id));
            
        return modelMapper.map(hotel, HotelDto.class);
    }

    public HotelDto getHotelByUserId(Integer userId) {
        Hotel hotel = hotelRepository.findByUserId(userId);
        if (hotel == null) {
            throw new NotFoundException("Không tìm thấy khách sạn với userId: " + userId);
        }
        // Get images
        List<Image> images = imageRepository.findAllImagesByHotelId(hotel.getId());
        HotelDto hotelDto = modelMapper.map(hotel, HotelDto.class);
        hotelDto.setImages(List.of(modelMapper.map(images, ImageDto[].class)));
        return hotelDto;
    }
}
