package hotelmanager.demo.services.bookings;

import java.util.List;
import java.util.stream.Collectors;

import hotelmanager.demo.dto.BookingServiceOrderDto;
import hotelmanager.demo.models.booking.BookingServiceOrder;
import hotelmanager.demo.models.enums.BookingServiceOrderStatus;
import hotelmanager.demo.repositories.BookingServiceOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookingServiceItemService {
    @Autowired
    private BookingServiceOrderRepository bookingServiceOrderRepository;

    private ModelMapper modelMapper = new ModelMapper();
    
    // get booking service order new
    @Transactional
    public List<BookingServiceOrderDto> getBookingServiceOrderNew(Integer hotelId) {
        List<BookingServiceOrder> bookingServiceOrders = bookingServiceOrderRepository.findByHotelIdAndStatus(hotelId, BookingServiceOrderStatus.NEW);
        return bookingServiceOrders.stream()
                .map(bookingServiceOrder -> modelMapper.map(bookingServiceOrder, BookingServiceOrderDto.class))
                .collect(Collectors.toList());
    }
}
