package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.bookingDtos.IBookingConsumableDto;
import hotelmanager.demo.models.booking.BookingConsumables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingConsumableRepository extends JpaRepository<BookingConsumables, Integer> {
    

    @Query(value = """
            SELECT 
                bc.id AS id,
                c.name AS name,
                c.consumable_category_id AS consumableCategoryId,
                c.unit AS unit,
                c.expiry_date AS expiryDate,
                c.barcode AS barcode,
                c.description AS description,
                bc.quantity_used AS quantityUsed,
                bc.total_price AS totalPrice,
                c.id AS consumableId
            FROM booking_consumables bc
            JOIN consumables c ON bc.consumable_id = c.id
            WHERE bc.booking_id = :bookingId
            """, nativeQuery = true)
    List<IBookingConsumableDto> findByBookingId(@Param("bookingId") Integer bookingId);
    
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM booking_consumables WHERE booking_id = :bookingId", nativeQuery = true)
    void deleteAllByBookingId(@Param("bookingId") Integer bookingId);
}
