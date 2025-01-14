package hotelmanager.demo.controllers;


import hotelmanager.demo.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendBookingConfirmation(@RequestParam String to) {
//        try {
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("customerName", "Test name");
//            variables.put("bookingId", "12345");
//            variables.put("hotelName", "Hotel ABCdssd");
//            variables.put("checkInDate", "2025-01-11");
//            variables.put("checkOutDate", "2025-01-15");
//
//            emailService.sendBookingConfirmationEmail(to, variables);
//            return "Email sent successfully!";
//        } catch (MessagingException e) {
//            e.printStackTrace();
            return "Failed to send email!";
//        }
    }
}