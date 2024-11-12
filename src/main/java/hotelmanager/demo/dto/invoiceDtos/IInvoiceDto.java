package hotelmanager.demo.dto.invoiceDtos;

public interface IInvoiceDto {
    Double getTotalAmount();
    String getPaymentMethod();
    Integer getBookingId();
    Integer getCustomerId();
    Long getIssueDate();
    String getPaymentStatus();
}
