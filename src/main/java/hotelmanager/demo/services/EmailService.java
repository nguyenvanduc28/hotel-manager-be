package hotelmanager.demo.services;
import hotelmanager.demo.dto.BookingServiceOrderDto;
import hotelmanager.demo.dto.HotelDto;
import hotelmanager.demo.dto.bookingDtos.BookingDto;
import hotelmanager.demo.dto.roomDtos.RoomDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.repositories.HotelRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private HotelRepository hotelRepository;
    public void sendBookingConfirmationEmail(String to, BookingDto bookingDto, Integer hotelId) throws MessagingException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn với ID: " + hotelId));

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Xác nhận đặt phòng");
        helper.setFrom("Booking <bookinghotelhgvn.com>");

        Map<String, Object> variables = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String bookingDateFormatted = dateFormat.format(new Date(bookingDto.getBookingDate() * 1000));
        String checkInDateFormatted = dateFormat.format(new Date(bookingDto.getCheckInDate() * 1000));
        String checkOutDateFormatted = dateFormat.format(new Date(bookingDto.getCheckOutDate() * 1000));

        variables.put("customerName", bookingDto.getCustomer().getName());
        variables.put("bookingDate", bookingDateFormatted);
        variables.put("checkInDate", checkInDateFormatted);
        variables.put("checkOutDate", checkOutDateFormatted);
        variables.put("numberOfAdults", bookingDto.getNumberOfAdults());
        variables.put("numberOfChildren", bookingDto.getNumberOfChildren());

        variables.put("hotelName", hotel.getName());
        variables.put("hotelRating", hotel.getRating());
        variables.put("hotelCity", hotel.getCity());
        variables.put("hotelAddress", hotel.getAddress());
        variables.put("hotelPhoneNumber", hotel.getPhoneNumber());
        variables.put("hotelEmail", hotel.getEmail());
        variables.put("hotelWebsite", hotel.getWebsiteUrl());

        List<Map<String, Object>> rooms = new ArrayList<>();
        for (RoomDto roomDto : bookingDto.getRooms()) {
            Map<String, Object> roomDetails = new HashMap<>();
            roomDetails.put("roomName", roomDto.getRoomNumber());
            roomDetails.put("roomType", roomDto.getRoomType().getName());
            roomDetails.put("pricePerNight", roomDto.getRoomType().getBasePricePerNight());
            rooms.add(roomDetails);
        }
        variables.put("rooms", rooms);

        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("booking-confirmation-vi", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }


    public void sendNewOrderConfirmationEmail(String to, BookingServiceOrderDto bookingServiceOrderDto, Integer hotelId) throws MessagingException {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách sạn với ID: " + hotelId));

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Thông báo Đơn hàng Mới");
        helper.setFrom("HotelBooking <support@hotel.com>");

        Map<String, Object> variables = new HashMap<>();
        variables.put("orderCreatedAt", new SimpleDateFormat("HH:mm dd-MM-yyyy").format(new Date(bookingServiceOrderDto.getOrderCreatedAt() * 1000)));
        variables.put("status", bookingServiceOrderDto.getStatus());
        variables.put("note", bookingServiceOrderDto.getNote());
        variables.put("orderItems", bookingServiceOrderDto.getOrderItems());
        variables.put("totalPrice", bookingServiceOrderDto.getTotalPrice());

        variables.put("hotelName", hotel.getName());
        variables.put("hotelCity", hotel.getCity());
        variables.put("hotelAddress", hotel.getAddress());
        variables.put("hotelPhoneNumber", hotel.getPhoneNumber());
        variables.put("hotelEmail", hotel.getEmail());
        variables.put("hotelWebsite", hotel.getWebsiteUrl());
        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("new-order-notification", context);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }


}