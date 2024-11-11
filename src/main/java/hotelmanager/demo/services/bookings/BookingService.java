package hotelmanager.demo.services.bookings;

import hotelmanager.demo.dto.bookingDtos.*;
import hotelmanager.demo.dto.roomDtos.IConsumableDto;
import hotelmanager.demo.dto.roomDtos.IEquipmentDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.*;
import hotelmanager.demo.models.enums.BookingStatus;
import hotelmanager.demo.models.enums.EquipmentStatus;
import hotelmanager.demo.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ConsumableRepository consumableRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingConsumableRepository bookingConsumableRepository;
    @Autowired
    private BookingEquipmentDamagedRepository bookingEquipmentDamagedRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDto> bookingDtos = new ArrayList<>();

        for (Booking booking: bookings) {
            BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
            enrichBookingDto(bookingDto);
            bookingDtos.add(bookingDto);
        }

        return bookingDtos;
    }

    private void enrichBookingDto(BookingDto bookingDto) {
        // Get consumables used
        List<IBookingConsumableDto> consumables = bookingConsumableRepository.findByBookingId(bookingDto.getId());
        bookingDto.setConsumablesUsed(List.of(modelMapper.map(consumables, BookingConsumableDto[].class)));

        // Get damaged equipment
        List<IBookingEquipmentDamagedDto> damagedEquipment = bookingEquipmentDamagedRepository.findByBookingId(bookingDto.getId());
        bookingDto.setEquipmentDamagedList(List.of(modelMapper.map(damagedEquipment, BookingEquipmentDamagedDto[].class)));
    }

    public List<BookingDto> getSortedBookings() {
        List<Booking> bookings = bookingRepository.findAllBookingsSortedByStatusAndDate();
        List<BookingDto> bookingDtos = List.of(modelMapper.map(bookings, BookingDto[].class));
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
    }

    public List<BookingDto> getAllBookingByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return getAllBooking();
        }
        List<BookingDto> bookingDtos = List.of(modelMapper.map(bookingRepository.searchByStatus(status), BookingDto[].class));
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
    }

    public List<BookingDto> getAllBookingByCusNam(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return getAllBooking();
        }
        List<BookingDto> bookingDtos = List.of(modelMapper.map(bookingRepository.searchByCustomerName(customerName), BookingDto[].class));
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
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

    @Transactional(readOnly = true)
    public BookingDto getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        enrichBookingDto(bookingDto);
        
        return bookingDto;
    }

    @Transactional
    public List<BookingConsumableDto> addBookingConsumables(Integer bookingId, List<BookingConsumables> consumableDtos) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        List<BookingConsumables> bookingConsumablesList = new ArrayList<>();
        
        for (BookingConsumables consumableDto : consumableDtos) {
            BookingConsumables bookingConsumable = new BookingConsumables();
            bookingConsumable.setBookingId(bookingId);
            bookingConsumable.setConsumableId(consumableDto.getId());
            bookingConsumable.setQuantityUsed(consumableDto.getQuantityUsed());
            bookingConsumable.setTotalPrice(consumableDto.getTotalPrice());
            bookingConsumable.setConsumableId(consumableDto.getConsumableId());
            bookingConsumablesList.add(bookingConsumable);
        }
        
        List<BookingConsumables> savedConsumables = bookingConsumableRepository.saveAll(bookingConsumablesList);
        return List.of(modelMapper.map(savedConsumables, BookingConsumableDto[].class));
    }

    @Transactional
    public BookingConsumableDto addBookingConsumable(Integer bookingId, BookingConsumables consumable) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        BookingConsumables bookingConsumable = new BookingConsumables();
        bookingConsumable.setBookingId(bookingId);
        bookingConsumable.setConsumableId(consumable.getId());
        bookingConsumable.setQuantityUsed(consumable.getQuantityUsed());
        bookingConsumable.setTotalPrice(consumable.getTotalPrice());
        bookingConsumable.setConsumableId(consumable.getConsumableId());

        BookingConsumables savedConsumable = bookingConsumableRepository.save(bookingConsumable);
        return modelMapper.map(savedConsumable, BookingConsumableDto.class);
    }

    @Transactional
    public List<BookingEquipmentDamagedDto> addBookingEquipmentDamaged(Integer bookingId, 
            List<BookingEquipmentDamaged> equipmentDamagedDtos) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        List<BookingEquipmentDamaged> bookingEquipmentDamagedList = new ArrayList<>();
        
        for (BookingEquipmentDamaged damagedDto : equipmentDamagedDtos) {
            BookingEquipmentDamaged damaged = new BookingEquipmentDamaged();
            damaged.setBookingId(bookingId);
            damaged.setEquipmentId(damagedDto.getId());
            damaged.setDamageFee(damagedDto.getDamageFee());
            damaged.setDamageDescription(damagedDto.getDamageDescription());
            damaged.setEquipmentId(damagedDto.getEquipmentId());
            bookingEquipmentDamagedList.add(damaged);
        }
        
        List<BookingEquipmentDamaged> savedDamaged = bookingEquipmentDamagedRepository.saveAll(bookingEquipmentDamagedList);
        return List.of(modelMapper.map(savedDamaged, BookingEquipmentDamagedDto[].class));
    }

    @Transactional
    public BookingEquipmentDamagedDto addBookingEquipmentDamaged(Integer bookingId, 
            BookingEquipmentDamaged damagedDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        BookingEquipmentDamaged damaged = new BookingEquipmentDamaged();
        damaged.setBookingId(bookingId);
        damaged.setEquipmentId(damagedDto.getId());
        damaged.setDamageFee(damagedDto.getDamageFee());
        damaged.setDamageDescription(damagedDto.getDamageDescription());
        damaged.setEquipmentId(damagedDto.getEquipmentId());

        BookingEquipmentDamaged savedDamaged = bookingEquipmentDamagedRepository.save(damaged);
        return modelMapper.map(savedDamaged, BookingEquipmentDamagedDto.class);
    }

    @Transactional
    public BookingDto checkout(Integer bookingId, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));

        //add consumables used
        List<BookingConsumables> consumables = List.of(modelMapper.map(bookingDto.getConsumablesUsed(), BookingConsumables[].class));
        this.addBookingConsumables(bookingId, consumables);

        //update consumables in room
        for (BookingConsumableDto bookingConsumableDto:bookingDto.getConsumablesUsed()) {
            IConsumableDto consumableDto = consumableRepository.findConsumableById(bookingConsumableDto.getConsumableId());
            if (consumableDto == null) throw new NotFoundException("Không tìm thấy consumable id:"+bookingConsumableDto.getConsumableId());
            if (consumableDto.getQuantity() < bookingConsumableDto.getQuantityUsed())
                throw new RuntimeException("Số lượng sử dụng phải nhỏ hơn hoặc bằng số lượng sẵn có");
            int quantity_rest = consumableDto.getQuantity() - bookingConsumableDto.getQuantityUsed();

//            if (quantity_rest == 0); // chỗ này thực hiện xóa khỏi room
            consumableRepository.updateQuantity(consumableDto.getId(), quantity_rest);
            // cập nhật kho nữa
        }

        //add equipment damaged
        List<BookingEquipmentDamaged> equipmentDamageds = List.of(modelMapper.map(bookingDto.getEquipmentDamagedList(), BookingEquipmentDamaged[].class));
        this.addBookingEquipmentDamaged(bookingId, equipmentDamageds);
        // thực hiện cập nhật status của equipment trong room
        for (BookingEquipmentDamagedDto bookingEquipmentDamagedDto:bookingDto.getEquipmentDamagedList()) {
            IEquipmentDto equipmentDto = equipmentRepository.findEquipmentById(bookingEquipmentDamagedDto.getEquipmentId());
            if (equipmentDto == null) throw new NotFoundException("Không tìm thấy equipment id:"+bookingEquipmentDamagedDto.getEquipmentId());
            equipmentRepository.updateStatus(equipmentDto.getId(), EquipmentStatus.MAINTENANCE.name());
        }

        //thực hiện lưu hóa đơn TODO

        //update checkoutTime
        Long checkOutTime = Instant.now().getEpochSecond();
        bookingRepository.checkout(bookingId, checkOutTime);

        //update status booking
        bookingRepository.updateStatusBooking(bookingId, BookingStatus.CHECKED_OUT);


        return bookingDto;
    }
}
