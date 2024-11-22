package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.ServiceDto;
import hotelmanager.demo.dto.ServiceItemDto;
import hotelmanager.demo.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import hotelmanager.demo.services.ServiceService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@Controller
@RequestMapping("/admin/service")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping("/service-type-list")
    public ResponseEntity<ResponseObject> getServiceTypeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ServiceDto> results = serviceService.getServiceList(userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(results)
                .message("Found service type list")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/create-service-type")
    public ResponseEntity<ResponseObject> createServiceType(@RequestBody ServiceDto serviceDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        serviceService.createServiceType(serviceDto, userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Create service type successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/update-service-type")
    public ResponseEntity<ResponseObject> updateServiceType(@RequestBody ServiceDto serviceDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        serviceService.updateServiceType(serviceDto, userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Update service type successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("/service-item-list")
    public ResponseEntity<ResponseObject> getServiceItemList(@RequestParam int serviceTypeId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ServiceItemDto> results = serviceService.getAllServiceItemByServiceType(serviceTypeId, userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(results)
                .message("Found service item list")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/create-service-item")
    public ResponseEntity<ResponseObject> createServiceItem(@RequestBody ServiceItemDto serviceItemDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        serviceService.createServiceItem(serviceItemDto, userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Create service item successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/update-service-item")
    public ResponseEntity<ResponseObject> updateServiceItem(@RequestBody ServiceItemDto serviceItemDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        serviceService.updateServiceItem(serviceItemDto, userDetails.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Update service item successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
