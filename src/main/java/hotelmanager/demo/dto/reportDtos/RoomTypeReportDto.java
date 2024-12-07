package hotelmanager.demo.dto.reportDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeReportDto {
  private Integer roomTypeId; // ID loại phòng
  private String roomTypeName; // Tên loại phòng
  private Double totalRevenue; // Tổng doanh thu từ loại phòng
  private Integer totalBookings; // Tổng số lượt đặt phòng từ loại phòng
  private Long timePeriodStart; // Thời gian bắt đầu (dạng timestamp)
  private Long timePeriodEnd; // Thời gian kết thúc (dạng timestamp)
}
