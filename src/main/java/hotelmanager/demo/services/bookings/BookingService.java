package hotelmanager.demo.services.bookings;

import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.bookingDtos.CustomerDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Booking;
import hotelmanager.demo.models.BookingRoom;
import hotelmanager.demo.models.Customer;
import hotelmanager.demo.models.Room;
import hotelmanager.demo.models.enums.BookingStatus;
import hotelmanager.demo.repositories.BookingRepository;
import hotelmanager.demo.repositories.BookingRoomRepository;
import hotelmanager.demo.repositories.CustomerRepository;
import hotelmanager.demo.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDto> bookingDtos = new ArrayList<>();

        for (Booking booking: bookings) {
            BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
            bookingDtos.add(bookingDto);
        }

        return bookingDtos;
    }
    public List<BookingDto> getSortedBookings() {
        List<Booking> bookings  = bookingRepository.findAllBookingsSortedByStatusAndDate();
        return List.of(modelMapper.map(bookings, BookingDto[].class));
    }
    public List<BookingDto> getAllBookingByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return getAllBooking();
        }
        return List.of(modelMapper.map(bookingRepository.searchByStatus(status), BookingDto[].class));
    }
    public List<BookingDto> getAllBookingByCusNam(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return getAllBooking();
        }
        return List.of(modelMapper.map(bookingRepository.searchByCustomerName(customerName), BookingDto[].class));
    }
    @Transactional
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setEstimatedArrivalTime(bookingDto.getEstimatedArrivalTime());
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setIsGroup(bookingDto.getIsGroup());
        booking.setTotalCost(bookingDto.getTotalCost());
        booking.setStatus(bookingDto.getStatus());
        booking.setDeposit(bookingDto.getDeposit());
        booking.setCancellationPolicy(bookingDto.getCancellationPolicy());
        booking.setCanceledAt(bookingDto.getCanceledAt());
        booking.setIsGuaranteed(bookingDto.getIsGuaranteed());
        booking.setNumberOfAdults(booking.getNumberOfAdults());
        booking.setNumberOfChildren(bookingDto.getNumberOfChildren());

        Customer customer = customerRepository.findById(bookingDto.getCustomer().getId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng"));
        booking.setCustomer(customer);

        Booking booking1 = bookingRepository.save(booking);
        List<BookingRoom> bookingRooms = new ArrayList<>();
        for (RoomDto roomDto:bookingDto.getRooms()) {
            BookingRoom bookingRoom = new BookingRoom();
            Room room = roomRepository.findById(roomDto.getId())
                    .orElseThrow(() -> new NotFoundException("không tìm thấy room"));
            bookingRoom.setRoom(room);
            bookingRoom.setBooking(booking1);
            bookingRooms.add(bookingRoom);
        }
        bookingRoomRepository.saveAll(bookingRooms);

        BookingDto bookingDto1 = new BookingDto();
        bookingDto1.setId(booking1.getId());
        bookingDto1.setCheckInDate(booking1.getCheckInDate());
        bookingDto1.setCheckOutDate(booking1.getCheckOutDate());
        bookingDto1.setEstimatedArrivalTime(booking1.getEstimatedArrivalTime());
        bookingDto1.setBookingDate(booking1.getBookingDate());
        bookingDto1.setIsGroup(booking1.getIsGroup());
        bookingDto1.setTotalCost(booking1.getTotalCost());
        bookingDto1.setStatus(booking1.getStatus());
        bookingDto1.setDeposit(booking1.getDeposit());
        bookingDto1.setCancellationPolicy(booking1.getCancellationPolicy());
        bookingDto1.setCanceledAt(booking1.getCanceledAt());
        bookingDto1.setIsGuaranteed(booking1.getIsGuaranteed());
        bookingDto1.setCustomer(modelMapper.map(booking1.getCustomer(), CustomerDto.class));
        bookingDto1.setRooms(bookingDto.getRooms()); //xử lý hơi ngoo

        return bookingDto1;
    }

    public void confirmBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
        bookingRepository.updateStatusBooking(bookingId, BookingStatus.CONFIRMED);
    }
    @Transactional
    public void checkinBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
        bookingRepository.updateStatusBooking(bookingId, BookingStatus.CHECKED_IN);
        Long checkInTime = Instant.now().getEpochSecond();
        bookingRepository.checkin(bookingId, checkInTime);
    }
}
