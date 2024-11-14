package hotelmanager.demo.services.invoices;

import hotelmanager.demo.dto.bookingDtos.*;
import hotelmanager.demo.dto.invoiceDtos.IInvoiceDto;
import hotelmanager.demo.dto.invoiceDtos.InvoiceDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Invoice;
import hotelmanager.demo.models.enums.BookingStatus;
import hotelmanager.demo.repositories.BookingRepository;
import hotelmanager.demo.repositories.InvoiceRepository;
import hotelmanager.demo.services.bookings.BookingService;
import hotelmanager.demo.services.bookings.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional(readOnly = true)
    public List<InvoiceDto> getAllInvoices(Integer hotelId) {
        List<IInvoiceDto> invoices = invoiceRepository.findAllInvoiceDtos(hotelId);
        return convertToInvoiceDtoList(invoices);
    }

    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceById(Integer id) {
        IInvoiceDto invoice = invoiceRepository.findInvoiceDtoById(id);
        if (invoice == null) {
            throw new NotFoundException("Invoice not found");
        }
        return convertToInvoiceDto(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceDto> getInvoicesByCustomerId(Integer customerId) {
        List<IInvoiceDto> invoices = invoiceRepository.findInvoiceDtosByCustomerId(customerId);
        return convertToInvoiceDtoList(invoices);
    }

    @Transactional(readOnly = true)
    public InvoiceDto getInvoiceByBookingId(Integer bookingId) {
        IInvoiceDto invoice = invoiceRepository.findInvoiceDtoByBookingId(bookingId);
        return convertToInvoiceDto(invoice);
    }

    @Transactional
    public InvoiceDto createInvoice(BookingDto bookingDto, Integer hotelId) {
        if (isInvoiceExistsByBookingId(bookingDto.getId())) {
            throw new RuntimeException("Booking đã được thanh toán");
        }
        validateBooking(bookingDto);
        Invoice invoice = createInvoiceFromBooking(bookingDto, hotelId);
        Long issueDate = Instant.now().getEpochSecond();
        invoice.setIssueDate(issueDate);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        bookingRepository.updateStatusBooking(bookingDto.getId(), BookingStatus.COMPLETED);
        InvoiceDto invoiceDto = modelMapper.map(savedInvoice, InvoiceDto.class);
        enrichInvoiceDto(invoiceDto);
        return invoiceDto;
    }

    private void validateBooking(BookingDto bookingDto) {
        IBookingDto booking = bookingRepository.findBookingDtoById(bookingDto.getId());
        if (booking == null) {
            throw new NotFoundException("Không tìm thấy booking");
        }
        if (!booking.getStatus().equals(BookingStatus.AWAITING_PAYMENT)) {
            throw new RuntimeException("Trạng thái booking hiện tại không được phép thanh toán");
        }
    }

    private Invoice createInvoiceFromBooking(BookingDto bookingDto, Integer hotelId) {
        Invoice invoice = new Invoice();
        invoice.setBookingId(bookingDto.getId());
        invoice.setCustomerId(bookingDto.getCustomer().getId());
        invoice.setTotalAmount(calculateTotalAmount(bookingDto));
        invoice.setPaymentStatus("PAID");
        invoice.setHotelId(hotelId);
        return invoice;
    }

    private Double calculateTotalAmount(BookingDto bookingDto) {
        Double totalAmount = bookingDto.getTotalCost();
        totalAmount += calculateConsumablesCost(bookingDto);
        totalAmount += calculateEquipmentDamageCost(bookingDto);
        totalAmount -= bookingDto.getDeposit() != null ? bookingDto.getDeposit() : 0.0;
        return totalAmount;
    }

    private Double calculateConsumablesCost(BookingDto bookingDto) {
        if (bookingDto.getConsumablesUsed() == null) {
            return 0.0;
        }
        return bookingDto.getConsumablesUsed().stream()
                .mapToDouble(BookingConsumableDto::getTotalPrice)
                .sum();
    }

    private Double calculateEquipmentDamageCost(BookingDto bookingDto) {
        if (bookingDto.getEquipmentDamagedList() == null) {
            return 0.0;
        }
        return bookingDto.getEquipmentDamagedList().stream()
                .mapToDouble(BookingEquipmentDamagedDto::getDamageFee)
                .sum();
    }

    private List<InvoiceDto> convertToInvoiceDtoList(List<IInvoiceDto> invoices) {
        List<InvoiceDto> invoiceDtos = new ArrayList<>();
        for (IInvoiceDto invoice : invoices) {
            invoiceDtos.add(convertToInvoiceDto(invoice));
        }
        return invoiceDtos;
    }

    private InvoiceDto convertToInvoiceDto(IInvoiceDto invoice) {
        InvoiceDto invoiceDto = modelMapper.map(invoice, InvoiceDto.class);
        setBookingAndCustomer(invoiceDto, invoice);
        enrichInvoiceDto(invoiceDto);
        return invoiceDto;
    }

    private void setBookingAndCustomer(InvoiceDto invoiceDto, IInvoiceDto invoice) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(invoice.getBookingId());
        invoiceDto.setBooking(bookingDto);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(invoice.getCustomerId());
        invoiceDto.setCustomer(customerDto);
    }

    @Transactional
    private void enrichInvoiceDto(InvoiceDto invoiceDto) {
        enrichBookingInformation(invoiceDto);
        enrichCustomerInformation(invoiceDto);
    }

    private void enrichBookingInformation(InvoiceDto invoiceDto) {
        if (invoiceDto.getBooking() != null) {
            try {
                BookingDto bookingDto = bookingService.getBookingById(invoiceDto.getBooking().getId());
                invoiceDto.setBooking(bookingDto);
            } catch (NotFoundException ignored) {
            }
        }
    }

    private void enrichCustomerInformation(InvoiceDto invoiceDto) {
        if (invoiceDto.getCustomer() != null) {
            try {
                CustomerDto customerDto = customerService.getCustomerById(invoiceDto.getCustomer().getId());
                invoiceDto.setCustomer(customerDto);
            } catch (NotFoundException ignored) {
            }
        }
    }

    public Boolean isInvoiceExistsByBookingId(Integer bookingId) {
        return invoiceRepository.countByBookingId(bookingId) > 0;
    }
}
