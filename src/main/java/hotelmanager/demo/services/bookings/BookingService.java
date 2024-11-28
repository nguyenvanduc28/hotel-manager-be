package hotelmanager.demo.services.bookings;

import hotelmanager.demo.dto.*;
import hotelmanager.demo.dto.bookingDtos.*;
import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.*;
import hotelmanager.demo.models.enums.BookingStatus;
import hotelmanager.demo.models.enums.BookingServiceOrderStatus;

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
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ConsumableCategoryRepository consumableCategoryRepository;
    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;
    @Autowired
    private BookingConsumableRepository bookingConsumableRepository;
    @Autowired
    private BookingEquipmentDamagedRepository bookingEquipmentDamagedRepository;
    @Autowired
    private BookingServiceRepository bookingServiceRepository;
    @Autowired
    private ServiceItemRepository serviceItemRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private BookingServiceOrderRepository bookingServiceOrderRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<BookingDto> getAllBooking(Integer hotelId) {
        List<Booking> bookings = bookingRepository.findAllBookingsByHotelIdOrderByCreatedAtDesc(hotelId);
        List<BookingDto> bookingDtos = new ArrayList<>();

        for (Booking booking: bookings) {
            BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
            enrichBookingDto(bookingDto);
            bookingDtos.add(bookingDto);
        }

        return bookingDtos;
    }

    private void enrichBookingDto(BookingDto bookingDto) {
        // Get rooms
        List<IRoomDto> roomDtos = bookingRepository.findAllRoomsByBookingId(bookingDto.getId());
        List<RoomDto> mappedRooms = roomDtos.stream()
            .map(roomDto -> {
                RoomDto mappedRoom = modelMapper.map(roomDto, RoomDto.class);
                RoomType roomType = roomTypeRepository.findById(mappedRoom.getRoomType().getId())
                    .orElseThrow(() -> new NotFoundException("Room type not found"));
                mappedRoom.setRoomType(modelMapper.map(roomType, RoomTypeDto.class));
                return mappedRoom;
            })
            .toList();
        bookingDto.setRooms(mappedRooms);

        // Get consumables used
        List<IBookingConsumableDto> consumables = bookingConsumableRepository.findByBookingId(bookingDto.getId());
        List<BookingConsumableDto> consumableDtos = new ArrayList<>();
        for (IBookingConsumableDto consumable : consumables) {
            BookingConsumableDto consumableDto = modelMapper.map(consumable, BookingConsumableDto.class);
            
            // Get category for each consumable
            IConsumableDto consumableInfo = consumableRepository.findConsumableById(consumable.getConsumableId());
            if (consumableInfo != null && consumableInfo.getConsumableCategoryId() != null) {
                IConsumableCategoryDto categoryDto = consumableCategoryRepository.findCategoryById(consumableInfo.getConsumableCategoryId());
                if (categoryDto != null) {
                    consumableDto.setConsumableCategory(modelMapper.map(categoryDto, ConsumableCategoryDto.class));
                }
            }
            
            consumableDtos.add(consumableDto);
        }
        
        bookingDto.setConsumablesUsed(consumableDtos);

        // Get damaged equipment
        List<IBookingEquipmentDamagedDto> damagedEquipment = bookingEquipmentDamagedRepository.findByBookingId(bookingDto.getId());
        List<BookingEquipmentDamagedDto> damagedDtos = new ArrayList<>();
        
        for (IBookingEquipmentDamagedDto damaged : damagedEquipment) {
            BookingEquipmentDamagedDto damagedDto = modelMapper.map(damaged, BookingEquipmentDamagedDto.class);
            
            // Get category for each equipment
            IEquipmentDto equipmentInfo = equipmentRepository.findEquipmentById(damaged.getEquipmentId());
            if (equipmentInfo != null && equipmentInfo.getEquipmentCategoryId() != null) {
                IEquipmentCategoryDto category = equipmentCategoryRepository.findCategoryById(equipmentInfo.getEquipmentCategoryId());
                if (category != null) {
                    damagedDto.setEquipmentCategory(modelMapper.map(category, EquipmentCategoryDto.class));
                }
            }
            
            damagedDtos.add(damagedDto);
        }
        bookingDto.setEquipmentDamagedList(damagedDtos);

        // Get service items and orders
        BookingServiceEntity bookingService = bookingServiceRepository.findByBookingId(bookingDto.getId());
        if (bookingService != null) {
            BookingServiceDto bookingServiceDto = modelMapper.map(bookingService, BookingServiceDto.class);
            
            // Get all service orders for this booking
            List<BookingServiceOrder> bookingServiceOrders = bookingServiceOrderRepository.findByBookingServiceId(bookingService.getId());
            List<BookingServiceOrderDto> bookingServiceOrderDtos = new ArrayList<>();

            // Process each service order
            for (BookingServiceOrder bookingServiceOrder : bookingServiceOrders) {
                BookingServiceOrderDto orderDto = modelMapper.map(bookingServiceOrder, BookingServiceOrderDto.class);
                
                // Get all order items for this service order
                List<OrderItem> orderItems = orderItemRepository.findByOrderId(bookingServiceOrder.getId());
                List<OrderItemDto> orderItemDtos = new ArrayList<>();

                // Process each order item
                for (OrderItem orderItem : orderItems) {
                    OrderItemDto itemDto = modelMapper.map(orderItem, OrderItemDto.class);
                    
                    // Get and map service item details
                    ServiceItem serviceItem = serviceItemRepository.findById(orderItem.getServiceItemId())
                            .orElseThrow(() -> new NotFoundException("Service item not found"));
                    ServiceItemDto serviceItemDto = modelMapper.map(serviceItem, ServiceItemDto.class);
                    
                    // Get and map service type
                    ServiceHotel serviceType = serviceRepository.findById(serviceItem.getServiceTypeId())
                            .orElseThrow(() -> new NotFoundException("Service type not found"));
                    ServiceDto serviceTypeDto = modelMapper.map(serviceType, ServiceDto.class);
                    
                    serviceItemDto.setServiceType(serviceTypeDto);
                    itemDto.setServiceItem(serviceItemDto);
                    orderItemDtos.add(itemDto);
                }
                
                orderDto.setOrderItems(orderItemDtos);
                bookingServiceOrderDtos.add(orderDto);
            }

            bookingServiceDto.setServiceOrders(bookingServiceOrderDtos);
            bookingDto.setServicesUsed(bookingServiceDto);
        }
    }

    public List<BookingDto> getSortedBookings(Integer hotelId) {
        List<IBookingDto> bookings = bookingRepository.findAllBookingsSortedByStatusAndDate(hotelId);
        List<BookingDto> bookingDtos = List.of(modelMapper.map(bookings, BookingDto[].class));
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
    }

    public List<BookingDto> getAllBookingByStatus(String status, Integer hotelId) {
        if (status == null || status.trim().isEmpty()) {
            return getAllBooking(hotelId);
        }
        List<BookingDto> bookingDtos = List.of(modelMapper.map(bookingRepository.searchByStatus(status, hotelId), BookingDto[].class));
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
    }

    public List<BookingDto> getAllBookingByCusName(String customerName, Integer hotelId) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return getAllBooking(hotelId);
        }
        List<IBookingDto> bookings = bookingRepository.searchByCustomerName(customerName, hotelId);
        List<BookingDto> bookingDtos = new ArrayList<>();
        for (IBookingDto booking : bookings) {
            BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
            CustomerDto customerDto = modelMapper.map(customerRepository.findById(booking.getCustomerId())
                    .orElseThrow(() -> new NotFoundException("Customer not found")), CustomerDto.class);
            bookingDto.setCustomer(customerDto);
            bookingDtos.add(bookingDto);
        }
        bookingDtos.forEach(this::enrichBookingDto);
        return bookingDtos;
    }

    @Transactional
    public BookingDto createBooking(BookingDto bookingDto, Integer hotelId) {
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
        booking.setHotelId(hotelId);

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

        // Create booking service
        BookingServiceEntity bookingService = new BookingServiceEntity();
        bookingService.setBookingId(booking1.getId());
        bookingService.setTotalPrice(0L);
        bookingServiceRepository.save(bookingService);

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

    @Transactional
    public void confirmBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
        bookingRepository.updateStatusBooking(bookingId, BookingStatus.CONFIRMED);
        Long confirmTime = Instant.now().getEpochSecond();
        bookingRepository.confirm(bookingId, confirmTime);
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
    public BookingDto checkout(Integer bookingId, BookingDto bookingDto, int hotelId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
        boolean isUpdate = booking.getStatus().equals(BookingStatus.AWAITING_PAYMENT);

        // Nếu là cập nhật, xóa dữ liệu cũ trước
        if (isUpdate) {
            // Xóa consumables cũ
            List<IBookingConsumableDto> oldConsumables = bookingConsumableRepository.findByBookingId(bookingId);
            for (IBookingConsumableDto oldConsumable : oldConsumables) {
                // Hoàn trả số lượng về room
                IConsumableDto consumableDto = consumableRepository.findConsumableById(oldConsumable.getConsumableId());
                if (consumableDto != null) {
                    int newQuantity = consumableDto.getQuantity() + oldConsumable.getQuantityUsed();
                    consumableRepository.updateQuantity(consumableDto.getId(), newQuantity);
                }
            }
            bookingConsumableRepository.deleteAllByBookingId(bookingId);

            // Xóa equipment damaged cũ
            List<IBookingEquipmentDamagedDto> oldDamaged = bookingEquipmentDamagedRepository.findByBookingId(bookingId);
            for (IBookingEquipmentDamagedDto damaged : oldDamaged) {
                // Reset status equipment về AVAILABLE
                equipmentRepository.updateStatus(damaged.getEquipmentId(), EquipmentStatus.AVAILABLE.name());
            }
            bookingEquipmentDamagedRepository.deleteAllByBookingId(bookingId);

        }

        // Thêm consumables mới
        List<BookingConsumables> consumables = List.of(modelMapper.map(bookingDto.getConsumablesUsed(), BookingConsumables[].class));
        this.addBookingConsumables(bookingId, consumables);

        // Cập nhật consumables trong room
        for (BookingConsumableDto bookingConsumableDto : bookingDto.getConsumablesUsed()) {
            IConsumableDto consumableDto = consumableRepository.findConsumableById(bookingConsumableDto.getConsumableId());
            if (consumableDto == null) 
                throw new NotFoundException("Không tìm thấy consumable id:" + bookingConsumableDto.getConsumableId());
            if (consumableDto.getQuantity() < bookingConsumableDto.getQuantityUsed())
                throw new RuntimeException("Số lượng sử dụng phải nhỏ hơn hoặc bằng số lượng sẵn có");
            
            int quantity_rest = consumableDto.getQuantity() - bookingConsumableDto.getQuantityUsed();
            consumableRepository.updateQuantity(consumableDto.getId(), quantity_rest);
        }

        // Thêm equipment damaged mới
        List<BookingEquipmentDamaged> equipmentDamageds = List.of(modelMapper.map(bookingDto.getEquipmentDamagedList(), BookingEquipmentDamaged[].class));
        this.addBookingEquipmentDamaged(bookingId, equipmentDamageds);

        // Cập nhật status của equipment trong room
        for (BookingEquipmentDamagedDto bookingEquipmentDamagedDto : bookingDto.getEquipmentDamagedList()) {
            IEquipmentDto equipmentDto = equipmentRepository.findEquipmentById(bookingEquipmentDamagedDto.getEquipmentId());
            if (equipmentDto == null) 
                throw new NotFoundException("Không tìm thấy equipment id:" + bookingEquipmentDamagedDto.getEquipmentId());
            equipmentRepository.updateStatus(equipmentDto.getId(), EquipmentStatus.MAINTENANCE.name());
        }

        // Cập nhật thời gian checkout nếu là lần đầu checkout
        if (!isUpdate) {
            Long checkOutTime = Instant.now().getEpochSecond();
            bookingRepository.checkout(bookingId, checkOutTime);
            bookingRepository.updateStatusBooking(bookingId, BookingStatus.AWAITING_PAYMENT);
        }

        return bookingDto;
    }

    @Transactional
    public void unConfirm(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        if (!booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            throw new RuntimeException("Booking phải ở trạng thái CONFIRMED");
        }

        Long currentTime = Instant.now().getEpochSecond();
        Long confirmedTime = booking.getConfirmedTime();

        if (confirmedTime == null || (currentTime - confirmedTime) > 600) { // 600 seconds = 10 minutes
            throw new RuntimeException("Chỉ có thể hủy xác nhận trong vòng 10 phút sau khi xác nhận");
        }

        bookingRepository.updateStatusBooking(bookingId, BookingStatus.PENDING);
        bookingRepository.confirm(bookingId, null);
    }

    @Transactional
    public void unCheckin(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking"));
                
        if (!booking.getStatus().equals(BookingStatus.CHECKED_IN)) {
            throw new RuntimeException("Booking phải ở trạng thái CHECKED_IN");
        }

        Long currentTime = Instant.now().getEpochSecond();
        Long checkedInTime = booking.getCheckInTime();
        
        if (checkedInTime == null || (currentTime - checkedInTime) > 600) { // 600 seconds = 10 minutes
            throw new RuntimeException("Chỉ có thể hủy check-in trong vòng 10 phút sau khi check-in");
        }

        bookingRepository.updateStatusBooking(bookingId, BookingStatus.CONFIRMED);
        bookingRepository.checkin(bookingId, null);
    }

    @Transactional
    public BookingServiceDto getServicesByBookingId(Integer bookingId) {
        // lấy booking service và chuyển đổi thành dto
        BookingServiceEntity bookingService = bookingServiceRepository.findByBookingId(bookingId);
        if (bookingService == null) {
            throw new NotFoundException("Không tìm thấy booking service");
        }

        BookingServiceDto bookingServiceDto = modelMapper.map(bookingService, BookingServiceDto.class);

        // lấy tất cả service order cho booking này
        List<BookingServiceOrder> bookingServiceOrders = bookingServiceOrderRepository.findByBookingServiceId(bookingService.getId());
        List<BookingServiceOrderDto> bookingServiceOrderDtos = new ArrayList<>();

        // xử lý mỗi service order
        for (BookingServiceOrder bookingServiceOrder : bookingServiceOrders) {
            BookingServiceOrderDto orderDto = modelMapper.map(bookingServiceOrder, BookingServiceOrderDto.class);
            
            // lấy tất cả order item cho service order này
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(bookingServiceOrder.getId());
            List<OrderItemDto> orderItemDtos = new ArrayList<>();

            // xử lý mỗi order item
            for (OrderItem orderItem : orderItems) {
                OrderItemDto itemDto = modelMapper.map(orderItem, OrderItemDto.class);
                
                // lấy và chuyển đổi service item details
                ServiceItem serviceItem = serviceItemRepository.findById(orderItem.getServiceItemId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy service item"));
                ServiceItemDto serviceItemDto = modelMapper.map(serviceItem, ServiceItemDto.class);
                
                // lấy và chuyển đổi service type
                ServiceHotel serviceType = serviceRepository.findById(serviceItem.getServiceTypeId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy loại dịch vụ"));
                ServiceDto serviceTypeDto = modelMapper.map(serviceType, ServiceDto.class);
                
                serviceItemDto.setServiceType(serviceTypeDto);
                itemDto.setServiceItem(serviceItemDto);
                orderItemDtos.add(itemDto);
            }
            
            orderDto.setOrderItems(orderItemDtos);
            bookingServiceOrderDtos.add(orderDto);
        }

        bookingServiceDto.setServiceOrders(bookingServiceOrderDtos);
        return bookingServiceDto;
    }

    @Transactional
    public BookingServiceOrderDto createNewOrder(Integer bookingId, BookingServiceOrderDto bookingServiceOrderDto, int hotelId) {
        BookingServiceEntity bookingService = bookingServiceRepository.findByBookingId(bookingId);
        if (bookingService == null) {
            throw new NotFoundException("Không tìm thấy booking service");
        }

        BookingServiceOrder bookingServiceOrder = modelMapper.map(bookingServiceOrderDto, BookingServiceOrder.class);
        Long orderCreatedAt = Instant.now().getEpochSecond() * 1000;
        bookingServiceOrder.setBookingServiceId(bookingService.getId());
        bookingServiceOrder.setOrderCreatedAt(orderCreatedAt);
        bookingServiceOrder.setServiceTypeId(bookingServiceOrderDto.getServiceTypeId());
        bookingServiceOrder.setHotelId(hotelId);

        // Tính lại total price cho từng order item và tổng
        Long totalPrice = 0L;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (OrderItemDto itemDto : bookingServiceOrderDto.getOrderItems()) {
            // Lấy service item để kiểm tra giá
            ServiceItem serviceItem = serviceItemRepository.findById(itemDto.getServiceItem().getId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy service item"));
            
            // Tính lại total price cho order item
            Long itemTotalPrice = serviceItem.getPrice() * itemDto.getQuantity();
            itemDto.setTotalPrice(itemTotalPrice);
            totalPrice += itemTotalPrice;

            // Tạo và cập nhật order item
            OrderItem orderItem = modelMapper.map(itemDto, OrderItem.class);
            orderItem.setServiceItemId(itemDto.getServiceItem().getId());
            orderItem.setTotalPrice(itemTotalPrice);
            orderItems.add(orderItem);
        }

        // Cập nhật tổng giá và lưu booking service order
        bookingServiceOrder.setTotalPrice(totalPrice);
        BookingServiceOrder savedOrder = bookingServiceOrderRepository.save(bookingServiceOrder);

        // Cập nhật order id và lưu các order items
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(savedOrder.getId());
        }
        orderItemRepository.saveAll(orderItems);

        return modelMapper.map(savedOrder, BookingServiceOrderDto.class);
    }

    @Transactional
    public BookingServiceOrderDto updateOrder(Integer orderId, BookingServiceOrderDto bookingServiceOrderDto, int hotelId) {
        BookingServiceOrder bookingServiceOrder = bookingServiceOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking service order"));

        // Xóa order items cũ
        List<OrderItem> oldOrderItems = orderItemRepository.findByOrderId(orderId);
        orderItemRepository.deleteAll(oldOrderItems);

        // Tính lại total price cho từng order item và tổng
        Long totalPrice = 0L;
        List<OrderItem> newOrderItems = new ArrayList<>();
        
        for (OrderItemDto itemDto : bookingServiceOrderDto.getOrderItems()) {
            // Lấy service item để kiểm tra giá
            ServiceItem serviceItem = serviceItemRepository.findById(itemDto.getServiceItem().getId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy service item"));
            
            // Tính lại total price cho order item
            Long itemTotalPrice = serviceItem.getPrice() * itemDto.getQuantity();
            itemDto.setTotalPrice(itemTotalPrice);
            totalPrice += itemTotalPrice;

            // Tạo và cập nhật order item
            OrderItem orderItem = modelMapper.map(itemDto, OrderItem.class);
            orderItem.setServiceItemId(itemDto.getServiceItem().getId());
            orderItem.setOrderId(orderId);
            orderItem.setTotalPrice(itemTotalPrice);
            newOrderItems.add(orderItem);
        }

        // Cập nhật thông tin booking service order
        bookingServiceOrder.setTotalPrice(totalPrice);
        bookingServiceOrder.setNote(bookingServiceOrderDto.getNote());
        bookingServiceOrderRepository.save(bookingServiceOrder);

        // Lưu các order items mới
        orderItemRepository.saveAll(newOrderItems);

        return modelMapper.map(bookingServiceOrder, BookingServiceOrderDto.class);
    }

    @Transactional
    public void confirmServicedForServiceOrder(Integer orderId) {
        Long currentTime = Instant.now().getEpochSecond() * 1000;
        BookingServiceOrder order = bookingServiceOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking service order"));
        order.setServicedAt(currentTime);
        order.setStatus(BookingServiceOrderStatus.SERVICED);
        bookingServiceOrderRepository.save(order);
    }
    @Transactional
    public void changeStatusToOrderService(Integer orderId, String status) {
        Long currentTime = Instant.now().getEpochSecond() * 1000;
        BookingServiceOrder order = bookingServiceOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking service order"));

        if (BookingServiceOrderStatus.NEW.equals(status)) {
            order.setServicedAt(null);
        } else if (BookingServiceOrderStatus.SERVICED.equals(status)) {
            order.setServicedAt(currentTime);
        }
        
        order.setStatus(status);
        bookingServiceOrderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Integer orderId) {
        BookingServiceOrder order = bookingServiceOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy booking service order"));
                
        if (BookingServiceOrderStatus.SERVICED.equals(order.getStatus())) {
            throw new RuntimeException("Không thể xóa đơn hàng đã phục vụ");
        }

        orderItemRepository.deleteByOrderId(orderId);
        bookingServiceOrderRepository.deleteById(orderId);
    }

}
