package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.BookingServiceDto;
import hotelmanager.demo.dto.BookingServiceOrderDto;
import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.bookingDtos.BookingConsumableDto;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.bookingDtos.BookingEquipmentDamagedDto;
import hotelmanager.demo.models.BookingConsumables;
import hotelmanager.demo.models.BookingEquipmentDamaged;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.bookings.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllBookings(@AuthenticationPrincipal CustomUserDetails user) {
        List<BookingDto> bookingDtos = bookingService.getSortedBookings(user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDtos)
                .message("Fetched all booking")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("create")
    public ResponseEntity<ResponseObject> createBooking(
            @RequestBody @Valid BookingDto bookingDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer hotelId = null;
        if (user != null) {
            hotelId = user.getUser().getHotelId();
        } else {
            hotelId = bookingDto.hotelId;
        }
        BookingDto bookingDto1 = bookingService.createBooking(bookingDto, hotelId);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Booking created successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("search")
    public ResponseEntity<ResponseObject> getBookingByStatus(@RequestParam String status, @AuthenticationPrincipal CustomUserDetails user) {
        List<BookingDto> bookingDto1 = bookingService.getAllBookingByStatus(status, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Fetched all booking by status")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("search-cusname")
    public ResponseEntity<ResponseObject> getBookingByCusName(@RequestParam String customerName, @AuthenticationPrincipal CustomUserDetails user) {
        List<BookingDto> bookingDto1 = bookingService.getAllBookingByCusName(customerName, user.getUser().getHotelId());
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

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getBookingById(@PathVariable Integer id) {
        BookingDto bookingDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto)
                .message("Get booking by id")
                .responseCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/{bookingId}/consumables")
    public ResponseEntity<ResponseObject> addBookingConsumables(
            @PathVariable Integer bookingId,
            @RequestBody List<BookingConsumables> consumableDtos) {
        List<BookingConsumableDto> savedConsumables = bookingService.addBookingConsumables(bookingId, consumableDtos);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(savedConsumables)
                .message("All Created booking consumables")
                .responseCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/{bookingId}/consumable")
    public ResponseEntity<ResponseObject> addBookingConsumable(
            @PathVariable Integer bookingId,
            @RequestBody BookingConsumables consumable) {
        BookingConsumableDto savedConsumable = bookingService.addBookingConsumable(bookingId, consumable);
        return ResponseEntity.ok(
            ResponseObject.builder()
                    .data(savedConsumable)
                    .message("Created booking consumables")
                    .responseCode(HttpStatus.OK.value())
                    .build()
        );
    }

    @PostMapping("/{bookingId}/equipment-damaged")
    public ResponseEntity<ResponseObject> addBookingEquipmentDamaged(
            @PathVariable Integer bookingId,
            @RequestBody List<BookingEquipmentDamaged> damagedDtos) {
        List<BookingEquipmentDamagedDto> savedDamaged =
                bookingService.addBookingEquipmentDamaged(bookingId, damagedDtos);
        return ResponseEntity.ok(
            ResponseObject.builder()
                    .data(savedDamaged)
                    .message("Add damaged equipment successfully")
                    .responseCode(HttpStatus.OK.value())
                    .build()
        );
    }

    @PostMapping("/{bookingId}/equipment-damaged/single")
    public ResponseEntity<ResponseObject> addSingleBookingEquipmentDamaged(
            @PathVariable Integer bookingId,
            @RequestBody BookingEquipmentDamaged damagedDto) {
        BookingEquipmentDamagedDto savedDamaged =
                bookingService.addBookingEquipmentDamaged(bookingId, damagedDto);
        return ResponseEntity.ok(
            ResponseObject.builder()
                    .data(savedDamaged)
                    .message("Add damaged equipment successfully")
                    .responseCode(HttpStatus.OK.value())
                    .build()
        );
    }

    @PostMapping("/{bookingId}/checkout")
    public ResponseEntity<ResponseObject> checkoutBooking(
        @PathVariable Integer bookingId,
        @RequestBody BookingDto bookingDto,
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        BookingDto bookingDto1 = bookingService.checkout(bookingId, bookingDto, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingDto1)
                .message("Checkout booking successfully")
                .responseCode(HttpStatus.OK.value())
                .build()
        );
    }

    @PostMapping("/{bookingId}/unconfirm")
    public ResponseEntity<ResponseObject> unconfirmBooking(@PathVariable Integer bookingId) {
        bookingService.unConfirm(bookingId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Unconfirmed booking successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/{bookingId}/uncheckin")
    public ResponseEntity<ResponseObject> uncheckInBooking(@PathVariable Integer bookingId) {
        bookingService.unCheckin(bookingId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Unchecked-in booking successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/{bookingId}/services")
    public ResponseEntity<ResponseObject> getServicesByBookingId(@PathVariable Integer bookingId) {
        BookingServiceDto services = bookingService.getServicesByBookingId(bookingId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(services)
                .message("Get services by booking id")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/{orderId}/confirm-serviced")
    public ResponseEntity<ResponseObject> confirmServicedForServiceOrder(@PathVariable Integer orderId) {
        bookingService.confirmServicedForServiceOrder(orderId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Confirmed serviced for service order")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/{orderId}/delete")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable Integer orderId) {
        bookingService.deleteOrder(orderId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(null)
                .message("Deleted service order")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/{bookingId}/service-order")
    public ResponseEntity<ResponseObject> createNewOrder(
        @PathVariable Integer bookingId, 
        @RequestBody BookingServiceOrderDto bookingServiceOrderDto, 
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        BookingServiceOrderDto bookingServiceOrderDto1 = bookingService.createNewOrder(bookingId, bookingServiceOrderDto, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(bookingServiceOrderDto)
                .message("Created new service order")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/{orderId}/service-order")
    public ResponseEntity<ResponseObject> updateOrder(
        @PathVariable Integer orderId, 
        @RequestBody BookingServiceOrderDto bookingServiceOrderDto, 
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        BookingServiceOrderDto updatedOrder = bookingService.updateOrder(orderId, bookingServiceOrderDto, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedOrder)
                .message("Updated service order")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

}
