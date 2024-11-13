package hotelmanager.demo.services.bookings;

import hotelmanager.demo.models.Image;
import hotelmanager.demo.repositories.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
@Slf4j
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private Cloudinary cloudinary;
    
    public List<Image> findAllImagesByRoomId(Integer roomId) {
        return imageRepository.findAllImagesByRoomId(roomId);
    }
    
    public Image uploadImage(MultipartFile file) throws IOException {
        try {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "hotel-rooms",
                            "resource_type", "auto"
                    ));
            
            // Create new Image entity
            Image image = new Image();
            image.setUrl((String) uploadResult.get("secure_url"));
            image.setPublicId((String) uploadResult.get("public_id"));
            image.setType(file.getContentType());
            image.setSize(file.getSize());
            image.setFileName(file.getOriginalFilename());
            
            // Save to database
            return imageRepository.save(image);
        } catch (IOException e) {
            log.error("Error uploading image: ", e);
            throw e;
        }
    }

    public List<Image> uploadMultipleImages(List<MultipartFile> files) {
        return files.stream()
                .map(file -> {
                    try {
                        return uploadImage(file);
                    } catch (IOException e) {
                        log.error("Error uploading image: ", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void deleteImage(String publicId) throws IOException {
        try {
            // Delete from Cloudinary
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            
            // Delete from database
            imageRepository.deleteByPublicId(publicId);
        } catch (IOException e) {
            log.error("Error deleting image: ", e);
            throw e;
        }
    }
}
