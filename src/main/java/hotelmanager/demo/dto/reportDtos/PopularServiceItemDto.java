package hotelmanager.demo.dto.reportDtos;

import hotelmanager.demo.dto.ServiceItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularServiceItemDto {
  private ServiceItemDto serviceItem;
  private Integer usageCount; // Số lần sử dụng
}
