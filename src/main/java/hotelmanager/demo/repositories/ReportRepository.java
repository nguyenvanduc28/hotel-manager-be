package hotelmanager.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import hotelmanager.demo.models.booking.Booking;

@Repository
public interface ReportRepository extends JpaRepository<Booking, Integer> {
    // Tổng doanh thu
    @Query(value = """
        SELECT SUM(i.total_amount) FROM invoice i 
        WHERE i.issue_date BETWEEN ?1 AND ?2 AND i.hotel_id = ?3
    """, nativeQuery = true)
    Double getTotalRevenue(Long startDate, Long endDate, Integer hotelId);

    // Tổng số lượt đặt phòng
    @Query(value = """
        SELECT COUNT(*) FROM bookings b 
        WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3
    """, nativeQuery = true)
    Integer getTotalBookings(Long startDate, Long endDate, Integer hotelId);

    // Tổng số khách hàng
    @Query(value = """
        SELECT COUNT(DISTINCT b.customer_id) FROM bookings b 
        WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3
    """, nativeQuery = true)
    Integer getTotalCustomers(Long startDate, Long endDate, Integer hotelId);

    // Tổng số đơn đặt phòng đã hoàn thành, hủy
    @Query(value = """
        SELECT COUNT(*) FROM bookings b 
        WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3 AND b.status = ?4
    """, nativeQuery = true)
    Integer getBookingCountByStatus(Long startDate, Long endDate, Integer hotelId, String status);

    // Doanh thu từ dịch vụ
    @Query(value = """
        SELECT COALESCE(SUM(bso.total_price), 0) FROM bookings b
        JOIN booking_service_entity bs ON bs.booking_id = b.id
        JOIN booking_service_order bso ON bso.booking_service_id = bs.id
        WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3
    """, nativeQuery = true)
    Double getServiceRevenue(Long startDate, Long endDate, Integer hotelId);

    // Doanh thu từ phòng
    // @Query(value = """
    //     SELECT COALESCE(SUM(b.room_price), 0) FROM bookings b 
    //     WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3
    // """, nativeQuery = true)
    // Double getRoomRevenue(Long startDate, Long endDate, Integer hotelId);

    // Doanh thu từ đồ dùng tiêu hao trong phòng
    @Query(value = """
        SELECT COALESCE(SUM(bc.total_price), 0) FROM booking_consumables bc 
        JOIN bookings b ON bc.booking_id = b.id 
        WHERE b.booking_date BETWEEN ?1 AND ?2 AND b.hotel_id = ?3
    """, nativeQuery = true)
    Double getConsumableRevenue(Long startDate, Long endDate, Integer hotelId);

    // Tỷ lệ lấp đầy trung bình của loại phòng
    @Query(value = """
        SELECT COALESCE(
            AVG(
                CAST(
                    (SELECT COUNT(*) 
                    FROM bookings b2 
                    WHERE b2.room_id = r.id 
                    AND b2.status = 'Đã hoàn tất'
                    AND b2.booking_date BETWEEN :startDate AND :endDate)
                AS FLOAT) / 
                (
                    EXTRACT(EPOCH FROM (TO_TIMESTAMP(:endDate/1000) - TO_TIMESTAMP(:startDate/1000)))/86400
                )
            ) * 100, 
            0
        )
        FROM rooms r
        WHERE r.hotel_id = :hotelId
        AND r.room_type_id = :roomTypeId
        """, nativeQuery = true)
    Double getRoomTypeOccupancyRate(Long startDate, Long endDate, Integer hotelId, Integer roomTypeId);
}
