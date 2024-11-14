package hotelmanager.demo.controllers;

import hotelmanager.demo.models.Image;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.bookings.ImageService;
import hotelmanager.demo.dto.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ResponseObject> getAllImagesByRoomId(@PathVariable Integer roomId) {
        List<Image> images = imageService.findAllImagesByRoomId(roomId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(images)
                .message("Fetched all images for room")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> uploadImage(@RequestBody MultipartFile file, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            Image uploadedImage = imageService.uploadImage(file, user.getUser().getHotelId());
            return ResponseEntity.ok(ResponseObject.builder()
                    .data(uploadedImage)
                    .message("Image uploaded successfully")
                    .responseCode(HttpStatus.OK.value())
                    .build());
        } catch (IOException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Failed to upload image")
                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build());
        }
    }

    @PostMapping("/upload/multiple")
    public ResponseEntity<ResponseObject> uploadMultipleImages(@RequestBody List<MultipartFile> files, @AuthenticationPrincipal CustomUserDetails user) {
        List<Image> uploadedImages = imageService.uploadMultipleImages(files, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(uploadedImages)
                .message("Multiple images uploaded successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<ResponseObject> deleteImage(@PathVariable String publicId) {
        try {
            imageService.deleteImage(publicId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Image deleted successfully")
                    .responseCode(HttpStatus.OK.value())
                    .build());
        } catch (IOException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Failed to delete image")
                    .responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build());
        }
    }
} 