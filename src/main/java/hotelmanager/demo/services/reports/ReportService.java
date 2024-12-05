package hotelmanager.demo.services.reports;

import hotelmanager.demo.models.enums.BookingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import hotelmanager.demo.dto.reportDtos.GeneralReportDto;
import hotelmanager.demo.dto.reportDtos.ReportRequestDto;
import hotelmanager.demo.repositories.*;

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

    private ModelMapper modelMapper = new ModelMapper();

    public GeneralReportDto getGeneralReport(ReportRequestDto request, Integer hotelId) {
        GeneralReportDto generalReportDto = new GeneralReportDto();
        

        Double totalRevenue = reportRepository.getTotalRevenue(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        Integer totalBookings = reportRepository.getTotalBookings(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        Integer totalCustomers = reportRepository.getTotalCustomers(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        
        Integer completedBookings = reportRepository.getBookingCountByStatus(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId, BookingStatus.COMPLETED);
        Integer canceledBookings = reportRepository.getBookingCountByStatus(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId, BookingStatus.CANCELED);
        
        Double serviceRevenue = reportRepository.getServiceRevenue(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        Double consumableRevenue = reportRepository.getConsumableRevenue(request.getTimePeriodStart(), request.getTimePeriodEnd(), hotelId);
        
        Double averageRevenuePerBooking = totalBookings > 0 ? totalRevenue / totalBookings : 0.0;

        generalReportDto.setTotalRevenue(totalRevenue);
        generalReportDto.setTotalBookings(totalBookings);
        generalReportDto.setTotalCustomers(totalCustomers);
        generalReportDto.setAverageRevenuePerBooking(averageRevenuePerBooking);
        generalReportDto.setTotalCompletedBookings(completedBookings);
        generalReportDto.setTotalCanceledBookings(canceledBookings);
        generalReportDto.setServiceRevenue(serviceRevenue);
//        generalReportDto.setRoomRevenue(roomRevenue);
        generalReportDto.setConsumableRevenue(consumableRevenue);

        return generalReportDto;
    }
}
