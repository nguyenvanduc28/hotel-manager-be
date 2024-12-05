package hotelmanager.demo.dto.reportDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopularServiceItemDto {
  private Integer serviceItemId; // ID món ăn/dịch vụ
  private String serviceItemName; // Tên món ăn/dịch vụ
  private Integer usageCount; // Số lần sử dụng
}
