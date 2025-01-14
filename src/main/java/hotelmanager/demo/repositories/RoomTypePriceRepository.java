package hotelmanager.demo.repositories;

import hotelmanager.demo.models.room.RoomPrice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypePriceRepository extends JpaRepository<RoomPrice, Integer> {
    List<RoomPrice> findAllByRoomTypeId(Integer roomTypeId);

    @Query(value = """
        SELECT * FROM room_prices WHERE room_type_id = :roomTypeId AND date BETWEEN :checkInDate AND :checkOutDate
    """, nativeQuery = true)
    List<RoomPrice> findAllRoomPricesInRange(Integer roomTypeId, Long checkInDate, Long checkOutDate);

    @Query(value = """
        SELECT * FROM room_prices WHERE room_type_id = :roomTypeId AND date = :date
    """, nativeQuery = true)
    RoomPrice findByRoomTypeIdAndDate(Integer roomTypeId, Long date);
}
