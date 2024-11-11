package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query(value = """
            SELECT * FROM bookings b
            ORDER BY 
                CASE b.status 
                    WHEN 'Đã nhận phòng' THEN 1
                    WHEN 'Đã xác nhận' THEN 2
                    WHEN 'Đang chờ' THEN 3
                    WHEN 'Đã trả phòng' THEN 4
                    WHEN 'Đã hoàn tất' THEN 5
                    WHEN 'Đã hoàn tiền' THEN 6
                    WHEN 'Đang chờ thanh toán' THEN 7
                    WHEN 'Không đến' THEN 8
                    WHEN 'Đã hủy' THEN 9
                    ELSE 10
                END,
                COALESCE(b.check_in_date, b.booking_date) ASC
            """, nativeQuery = true)
    List<Booking> findAllBookingsSortedByStatusAndDate();
    @Query(value = """
            SELECT * FROM bookings b
            WHERE (:status IS NULL OR b.status = :status)
            ORDER BY COALESCE(b.check_in_date, b.booking_date) ASC
            """, nativeQuery = true)
    List<Booking> searchByStatus(@Param("status") String status);

    @Query(value = """
            SELECT b.* FROM bookings b
            LEFT JOIN customers c ON b.customer_id = c.id
            WHERE c.name LIKE %:customerName%
            ORDER BY COALESCE(b.check_in_date, b.booking_date) ASC
            """, nativeQuery = true)
    List<Booking> searchByCustomerName(@Param("customerName") String customerName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE bookings SET status = :status WHERE id = :bookingId", nativeQuery = true)
    void updateStatusBooking(@Param("bookingId") int bookingId, @Param("status") String status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE bookings SET check_in_time = :checkInTime WHERE id = :bookingId", nativeQuery = true)
    void checkin(@Param("bookingId") int bookingId, @Param("checkInTime") Long checkInTime);

//    @Query(value = """
//        SELECT b.*, c.name AS customer_name, c.phone AS customer_phone, c.email AS customer_email,
//               c.address AS customer_address, c.id_card AS customer_id_card
//        FROM bookings b
//        LEFT JOIN customers c ON b.customer_id = c.id
//        LEFT JOIN booking_rooms br ON b.id = br.booking_id
//        LEFT JOIN rooms r ON br.room_id = r.id
//        WHERE b.id = :bookingId
//        """, nativeQuery = true)
//    Booking findBookingById(@Param("bookingId") Integer bookingId);
    @Modifying
    @Query("UPDATE Booking b SET b.checkOutTime = :checkOutTime WHERE b.id = :bookingId")
    void checkout(@Param("bookingId") Integer bookingId, @Param("checkOutTime") Long checkOutTime);

}
