package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.reportDtos.GeneralReportDto;
import hotelmanager.demo.dto.reportDtos.ReportRequestDto;
import hotelmanager.demo.dto.reportDtos.ServiceReportDto;
import hotelmanager.demo.dto.reportDtos.RoomTypeReportDto;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.reports.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/general")
    public ResponseEntity<ResponseObject> getGeneralReport(@RequestBody ReportRequestDto request, @AuthenticationPrincipal CustomUserDetails user) {
        GeneralReportDto generalReport = reportService.getGeneralReport(request, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(generalReport)
                .message("Fetched general report")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/service")
    public ResponseEntity<ResponseObject> getServiceReport(
        @RequestBody ReportRequestDto request, 
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        ServiceReportDto serviceReport = reportService.getServiceReport(request, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(serviceReport)
                .message("Fetched service report")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/room-type")
    public ResponseEntity<ResponseObject> getRoomTypeReport(
        @RequestBody ReportRequestDto request, 
        @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<RoomTypeReportDto> roomTypeReport = reportService.getRoomTypeReport(request, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeReport)
                .message("Fetched room type report")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
