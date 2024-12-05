package hotelmanager.demo.dto.reportDtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceReportDto {
  private Integer serviceTypeId; // ID loại dịch vụ
  private String serviceTypeName; // Tên loại dịch vụ (Nhà hàng, quầy bar, dịch vụ phòng, ...)
  private Double totalRevenue; // Tổng doanh thu từ dịch vụ
  private Integer usageCount; // Số lần sử dụng dịch vụ
  private Long timePeriodStart; // Thời gian bắt đầu (dạng timestamp)
  private Long timePeriodEnd; // Thời gian kết thúc (dạng timestamp)
  private List<PopularServiceItemDto> popularServiceItems; // Danh sách món ăn/dịch vụ phổ biến
}
