package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(value = "SELECT * FROM image WHERE room_id = :roomId AND deleted = false", nativeQuery = true)
    List<Image> findAllImagesByRoomId(@Param("roomId") Integer roomId);

    @Query(value = "SELECT * FROM image WHERE hotel_id = :hotelId AND deleted = false", nativeQuery = true)
    List<Image> findAllImagesByHotelId(@Param("hotelId") Integer hotelId);

    @Modifying
    @Query(value = "DELETE FROM image WHERE public_id = :publicId AND deleted = false", nativeQuery = true)
    void deleteByPublicId(@Param("publicId") String publicId);

    @Modifying
    @Query(value = "UPDATE image SET room_id = NULL WHERE id = :imageId", nativeQuery = true)
    void deleteRoomIdByImageId(@Param("imageId") Integer imageId);

    @Modifying
    @Query(value = "UPDATE image SET hotel_id = NULL WHERE id = :imageId", nativeQuery = true)
    void deleteHotelIdByImageId(@Param("imageId") Integer imageId);

    @Modifying
    @Query(value = "UPDATE image SET room_id = :roomId WHERE id = :imageId", nativeQuery = true)
    void updateRoomIdByImageId(@Param("roomId") Integer roomId, @Param("imageId") Integer imageId);

    @Modifying
    @Query(value = "UPDATE image SET hotel_id = :hotelId WHERE id = :imageId", nativeQuery = true)
    void updateHotelIdByImageId(@Param("hotelId") Integer hotelId, @Param("imageId") Integer imageId);
}
