package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.invoiceDtos.InvoiceDto;
import hotelmanager.demo.services.invoices.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/getall")
    public ResponseEntity<ResponseObject> getAllInvoices() {
        List<InvoiceDto> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(invoices)
                .message("Fetched all invoices")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getInvoiceById(@PathVariable Integer id) {
        InvoiceDto invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(invoice)
                .message("Fetched invoice with ID: " + id)
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseObject> getInvoicesByCustomerId(@PathVariable Integer customerId) {
        List<InvoiceDto> invoices = invoiceService.getInvoicesByCustomerId(customerId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(invoices)
                .message("Fetched all invoices for customer ID: " + customerId)
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ResponseObject> getInvoiceByBookingId(@PathVariable Integer bookingId) {
        InvoiceDto invoice = invoiceService.getInvoiceByBookingId(bookingId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(invoice)
                .message("Fetched invoice with booking ID: " + bookingId)
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createInvoice(@RequestBody BookingDto bookingDto) {
        try {
            InvoiceDto invoice = invoiceService.createInvoice(bookingDto);
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(invoice)
                    .message("Tạo hóa đơn thành công")
                    .responseCode(HttpStatus.OK.value())
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .data(null)
                            .message(e.getMessage())
                            .responseCode(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
    }

    @GetMapping("/check-invoice-exists/{bookingId}")
    public ResponseEntity<ResponseObject> checkInvoiceExistsByBookingId(@PathVariable Integer bookingId) {
        Boolean exists = invoiceService.isInvoiceExistsByBookingId(bookingId);
        return ResponseEntity.ok(ResponseObject.builder().data(exists).build());
    }
} 
