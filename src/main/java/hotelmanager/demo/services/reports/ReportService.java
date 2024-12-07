package hotelmanager.demo.services.reports;

import hotelmanager.demo.dto.BookingServiceOrderDto;
import hotelmanager.demo.dto.OrderItemDto;
import hotelmanager.demo.dto.ServiceDto;
import hotelmanager.demo.dto.ServiceItemDto;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.reportDtos.*;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.dto.roomDtos.RoomTypeDto;
import hotelmanager.demo.models.BookingServiceOrder;
import hotelmanager.demo.models.RoomType;
import hotelmanager.demo.models.ServiceHotel;
import hotelmanager.demo.models.enums.BookingStatus;
import hotelmanager.demo.services.bookings.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import hotelmanager.demo.repositories.*;
import hotelmanager.demo.services.ServiceService;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ConsumableRepository consumableRepository;
    @Autowired
    private ConsumableCategoryRepository consumableCategoryRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private BookingServiceOrderRepository bookingServiceOrderRepository;
    @Autowired
    private ServiceRepository serviceTypeRepository;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private BookingService bookingService;

    private ModelMapper modelMapper = new ModelMapper();

    // Lấy báo cáo tổng quan
    public GeneralReportDto getGeneralReport(ReportRequestDto request, Integer hotelId) {
        GeneralReportDto generalReportDto = new GeneralReportDto();
        List<BookingDto> bookings = bookingService.getBookingsWithTimePeriod(
            request.getTimePeriodStart(),
            request.getTimePeriodEnd(),
            hotelId
        );
        // tổng doanh thu
        Double totalRevenue = bookings.stream()
            .mapToDouble(BookingDto::getTotalCost)
            .sum();
        // tổng số đơn đặt phòng
        Integer totalBookings = bookings.size();
        // tổng số khách hàng
        Integer totalCustomers = reportRepository.getTotalCustomers(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        // tổng số đơn đặt phòng đã hoàn thành
        Integer completedBookings = bookings.stream()
            .filter(booking -> booking.getStatus().equals(BookingStatus.COMPLETED))
            .collect(Collectors.toList())
            .size();
        // tổng số đơn đặt phòng đã hủy
        Integer canceledBookings = bookings.stream()
            .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELED))
            .collect(Collectors.toList())
            .size();
        // doanh thu từ phòng
        Double roomRevenue = 0.0;
        for (BookingDto booking : bookings) {
            for (RoomDto room : booking.getRooms()) {
                RoomTypeDto roomType = room.getRoomType();
                if (roomType.getPriceToday() != null) {
                    roomRevenue += roomType.getPriceToday();
                } else {
                    roomRevenue += roomType.getBasePricePerNight();
                }
            }
        }
        // doanh thu từ dịch vụ
        Double serviceRevenue = reportRepository.getServiceRevenue(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        // doanh thu từ đồ dùng tiêu hao trong phòng
        Double consumableRevenue = reportRepository.getConsumableRevenue(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        // doanh thu trung bình trên mỗi đơn đặt phòng
        Double averageRevenuePerBooking = totalBookings > 0 ? totalRevenue / totalBookings : 0.0;

        generalReportDto.setTotalRevenue(totalRevenue);
        generalReportDto.setTotalBookings(totalBookings);
        generalReportDto.setTotalCustomers(totalCustomers);
        generalReportDto.setAverageRevenuePerBooking(averageRevenuePerBooking);
        generalReportDto.setTotalCompletedBookings(completedBookings);
        generalReportDto.setTotalCanceledBookings(canceledBookings);
        generalReportDto.setServiceRevenue(serviceRevenue);
        generalReportDto.setRoomRevenue(roomRevenue);
        generalReportDto.setConsumableRevenue(consumableRevenue);

        return generalReportDto;
    }

    // Lấy báo cáo theo dịch vụ
    public ServiceReportDto getServiceReport(ReportRequestDto request, Integer hotelId) {
        ServiceReportDto serviceReportDto = new ServiceReportDto();
        
        // Get service type info first
        ServiceHotel serviceType = serviceTypeRepository.findById(request.getServiceTypeId())
            .orElseThrow(() -> new RuntimeException("Service type not found"));
        
        // Get all orders in time period
        List<BookingServiceOrderDto> orders = serviceService.getBookingServiceOrderByTimePeriod(
            hotelId,
            request.getServiceTypeId(),
            request.getTimePeriodStart(),
            request.getTimePeriodEnd()
        );
        
        // Calculate report metrics
        Double totalRevenue = 0.0;
        Map<Integer, PopularServiceItemDto> popularItemsMap = new HashMap<>();
        
        // Process each order
        for (BookingServiceOrderDto order : orders) {
            totalRevenue += order.getTotalPrice();
            
            // Process order items
            for (OrderItemDto item : order.getOrderItems()) {
                ServiceItemDto serviceItem = item.getServiceItem();
                Integer itemId = serviceItem.getId();
                
                // Update or create popular item entry
                PopularServiceItemDto popularItem = popularItemsMap.computeIfAbsent(
                    itemId,
                    k -> {
                        PopularServiceItemDto dto = new PopularServiceItemDto(
                            serviceItem,
                            0
                        );
                        return dto;
                    }
                );
                
                // Update metrics
                popularItem.setUsageCount(popularItem.getUsageCount() + item.getQuantity());
            }
        }
        
        // Build report
        serviceReportDto.setServiceTypeId(serviceType.getId());
        serviceReportDto.setServiceTypeName(serviceType.getName());
        serviceReportDto.setTotalRevenue(totalRevenue);
        serviceReportDto.setUsageCount(orders.size());
        serviceReportDto.setTimePeriodStart(request.getTimePeriodStart());
        serviceReportDto.setTimePeriodEnd(request.getTimePeriodEnd());
        serviceReportDto.setPopularServiceItems(new ArrayList<>(popularItemsMap.values()));
        
        return serviceReportDto;
    }

    // Lấy báo cáo theo loại phòng
    public List<RoomTypeReportDto> getRoomTypeReport(ReportRequestDto request, Integer hotelId) {
        List<RoomTypeReportDto> roomTypeReportDtos = new ArrayList<>();

        List<RoomType> roomTypes = roomTypeRepository.findAllByHotelId(hotelId);
        List<RoomTypeDto> roomTypeDtos = List.of(modelMapper.map(roomTypes, RoomTypeDto[].class));

        List<BookingDto> bookings = bookingService.getBookingsWithTimePeriod(
            request.getTimePeriodStart(),
            request.getTimePeriodEnd(),
            hotelId
        );

        // tạo map để theo dõi metrics cho mỗi loại phòng
        Map<Integer, RoomTypeReportDto> reportMap = new HashMap<>();

        // khởi tạo DTOs cho mỗi loại phòng
        for (RoomTypeDto roomType: roomTypeDtos) {
            RoomTypeReportDto reportDto = new RoomTypeReportDto();
            reportDto.setRoomTypeId(roomType.getId());
            reportDto.setRoomTypeName(roomType.getName());
            reportDto.setTimePeriodStart(request.getTimePeriodStart());
            reportDto.setTimePeriodEnd(request.getTimePeriodEnd());
            reportDto.setTotalRevenue(0.0);
            reportDto.setTotalBookings(0);
            reportMap.put(roomType.getId(), reportDto);
        }

        // xử lý đơn đặt phòng để tính toán metrics
        for (BookingDto booking : bookings) {
            for (RoomDto room : booking.getRooms()) {
                RoomTypeDto roomType = room.getRoomType();
                RoomTypeReportDto report = reportMap.get(roomType.getId());
                
                if (report != null) {
                    report.setTotalBookings(report.getTotalBookings() + 1);
                    if (roomType.getPriceToday() != null) {
                        report.setTotalRevenue(report.getTotalRevenue() + roomType.getPriceToday());
                    } else {
                        report.setTotalRevenue(report.getTotalRevenue() + roomType.getBasePricePerNight());
                    }
                }
            }
        }

        return new ArrayList<>(reportMap.values());
    }
}
