package hotelmanager.demo.dto.reportDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDto {
  private Long timePeriodStart; // Thời gian bắt đầu (dạng timestamp)
  private Long timePeriodEnd; // Thời gian kết thúc (dạng timestamp)
  private Integer serviceTypeId; // id loại dịch vụ
}
