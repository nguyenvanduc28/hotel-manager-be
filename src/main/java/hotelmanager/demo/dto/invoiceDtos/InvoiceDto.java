package hotelmanager.demo.dto.invoiceDtos;

import hotelmanager.demo.dto.BaseDto;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.bookingDtos.CustomerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDto extends BaseDto {
    private Double totalAmount;
    private String paymentMethod;
    private BookingDto booking;
    private CustomerDto customer;
    private Long issueDate;
    private String paymentStatus;
}
