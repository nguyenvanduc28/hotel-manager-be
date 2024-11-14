package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.HotelDto;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping("create")
    public ResponseEntity<ResponseObject> createHotel(
            @RequestBody @Valid HotelDto hotelDto
    ) {
        HotelDto createdHotel = hotelService.createHotel(hotelDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(createdHotel)
                .message("Hotel created successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ResponseObject> updateHotel(
            @PathVariable Integer id,
            @RequestBody @Valid HotelDto hotelDto
    ) {
        HotelDto updatedHotel = hotelService.updateHotel(id, hotelDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedHotel)
                .message("Hotel updated successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("get/{id}")
    public ResponseEntity<ResponseObject> getHotelById(@PathVariable Integer id) {
        HotelDto hotelDto = hotelService.getHotelById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(hotelDto)
                .message("Hotel fetched successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("get-info-hotel")
    public ResponseEntity<ResponseObject> getHotelByUserId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        HotelDto hotelDto = hotelService.getHotelByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(hotelDto)
                .message("Hotel fetched successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
