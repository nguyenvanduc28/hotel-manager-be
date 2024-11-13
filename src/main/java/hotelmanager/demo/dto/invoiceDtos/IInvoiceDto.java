package hotelmanager.demo.dto.invoiceDtos;

public interface IInvoiceDto {
    Integer getId();
    Double getTotalAmount();
    String getPaymentMethod();
    Integer getBookingId();
    Integer getCustomerId();
    Long getIssueDate();
    String getPaymentStatus();
}
