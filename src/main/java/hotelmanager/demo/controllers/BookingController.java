package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.bookingDtos.CustomerDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.services.bookings.BookingService;
import hotelmanager.demo.services.bookings.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllBookings() {
        List<BookingDto> bookingDtos = bookingService.getSortedBookings();

        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDtos)
                .message("Fetched all booking")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("create")
    public ResponseEntity<ResponseObject> createBooking(
            @RequestBody @Valid BookingDto bookingDto
    ) {
        BookingDto bookingDto1 = bookingService.createBooking(bookingDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Booking created successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("search")
    public ResponseEntity<ResponseObject> getBookingByStatus(@RequestParam String status) {
        List<BookingDto> bookingDto1 = bookingService.getAllBookingByStatus(status);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Fetched all booking by status")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("search-cusname")
    public ResponseEntity<ResponseObject> getBookingByCusName(@RequestParam String customerName) {
        List<BookingDto> bookingDto1 = bookingService.getAllBookingByCusNam(customerName);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Fetched all booking by customerName")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<ResponseObject> confirmBooking(@PathVariable Integer id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Confirmed booking")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("/checkin/{id}")
    public ResponseEntity<ResponseObject> checkinBooking(@PathVariable Integer id) {
        bookingService.checkinBooking(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Confirmed booking")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
