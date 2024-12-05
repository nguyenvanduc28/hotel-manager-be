package hotelmanager.demo.dto.reportDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralReportDto {
    private Double totalRevenue; // Tổng doanh thu
    private Integer totalBookings; // Tổng số lượt đặt phòng
    private Integer totalCustomers; // Tổng số khách hàng
    private Double averageRevenuePerBooking; // Doanh thu trung bình trên mỗi đơn đặt phòng
    private Integer totalCompletedBookings; // Tổng số đơn đặt phòng đã hoàn thành
    private Integer totalCanceledBookings; // Tổng số đơn đặt phòng đã hủy
    private Double serviceRevenue; // Doanh thu từ dịch vụ
    private Double roomRevenue; // Doanh thu từ phòng
    private Double consumableRevenue; // Doanh thu từ tiêu dùng nội bộ
}
