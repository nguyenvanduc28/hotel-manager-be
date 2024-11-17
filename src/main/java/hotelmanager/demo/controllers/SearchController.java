package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.HotelSearchResultDto;
import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    
    @GetMapping("/hotels")
    public ResponseEntity<ResponseObject> searchHotels(
        @RequestParam String location,
        @RequestParam Long checkInDate,
        @RequestParam Long checkOutDate
    ) {
        List<HotelSearchResultDto> results = searchService.searchHotels(
            location, 
            checkInDate, 
            checkOutDate
        );
        return ResponseEntity.ok(ResponseObject.builder()
                .data(results)
                .message("Found hotels matching search criteria")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
} 