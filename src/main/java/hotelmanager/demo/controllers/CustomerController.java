package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.bookingDtos.CustomerDto;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.bookings.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("create")
    public ResponseEntity<ResponseObject> createCustomer(
            @RequestBody @Valid CustomerDto customerDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Integer hotelId = null; 
        if (user != null) {
            hotelId = user.getUser().getHotelId();
        } else {
            hotelId = customerDto.hotelId;
        }
        CustomerDto createdCustomer = customerService.createCustomer(customerDto, hotelId);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(createdCustomer)
                .message("Customer created successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllCustomers(@AuthenticationPrincipal CustomUserDetails user) {
        List<CustomerDto> customerList = customerService.getAllCustomers(user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(customerList)
                .message("Fetched all customers")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("search")
    public ResponseEntity<ResponseObject> searchCustomersByName(@RequestParam String name, @AuthenticationPrincipal CustomUserDetails user) {
        List<CustomerDto> customerList = customerService.searchCustomersByNameOrPhoneNumber(name, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(customerList)
                .message("Fetched customers by name or phone number")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
