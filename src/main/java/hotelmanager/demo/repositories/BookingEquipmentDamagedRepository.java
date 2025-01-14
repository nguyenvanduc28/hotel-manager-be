package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.bookingDtos.IBookingEquipmentDamagedDto;
import hotelmanager.demo.models.booking.BookingEquipmentDamaged;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingEquipmentDamagedRepository extends JpaRepository<BookingEquipmentDamaged, Integer> {
    @Query(value = """
            SELECT 
                bed.id AS id,
                e.name AS name,
                e.equipment_category_id AS equipmentCategoryId,
                e.installation_date AS installationDate,
                e.barcode AS barcode,
                e.description AS description,
                bed.damage_fee AS damageFee,
                bed.damage_description AS damageDescription,
                e.id AS equipmentId
            FROM booking_equipment_damaged bed
            JOIN equipment e ON bed.equipment_id = e.id
            WHERE bed.booking_id = :bookingId
            """, nativeQuery = true)
    List<IBookingEquipmentDamagedDto> findByBookingId(@Param("bookingId") Integer bookingId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM booking_equipment_damaged WHERE booking_id = :bookingId", nativeQuery = true)
    void deleteAllByBookingId(@Param("bookingId") Integer bookingId);
}
