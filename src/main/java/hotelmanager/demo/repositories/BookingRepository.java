package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.bookingDtos.IBookingDto;
import hotelmanager.demo.dto.roomDtos.IRoomDto;
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
            SELECT b.id AS id, 
                   b.check_in_date AS checkInDate,
                   b.check_in_time AS checkInTime,
                   b.check_out_time AS checkOutTime,
                   b.check_out_date AS checkOutDate,
                   b.estimated_arrival_time AS estimatedArrivalTime,
                   b.booking_date AS bookingDate,
                   b.is_group AS isGroup,
                   b.total_cost AS totalCost,
                   b.status AS status,
                   b.deposit AS deposit,
                   b.cancellation_policy AS cancellationPolicy,
                   b.canceled_at AS canceledAt,
                   b.number_of_adults AS numberOfAdults,
                   b.number_of_children AS numberOfChildren,
                   b.is_guaranteed AS isGuaranteed
            FROM bookings b
            WHERE b.hotel_id = :hotelId AND b.deleted = false
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
    List<IBookingDto> findAllBookingsSortedByStatusAndDate(@Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT * FROM bookings b
            WHERE (:status IS NULL OR b.status = :status) AND b.hotel_id = :hotelId AND b.deleted = false
            ORDER BY COALESCE(b.check_in_date, b.booking_date) ASC
            """, nativeQuery = true)
    List<Booking> searchByStatus(@Param("status") String status, @Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT b.id AS id, 
                   b.check_in_date AS checkInDate,
                   b.check_in_time AS checkInTime,
                   b.check_out_time AS checkOutTime,
                   b.check_out_date AS checkOutDate,
                   b.estimated_arrival_time AS estimatedArrivalTime,
                   b.booking_date AS bookingDate,
                   b.is_group AS isGroup,
                   b.total_cost AS totalCost,
                   b.status AS status,
                   b.deposit AS deposit,
                   b.cancellation_policy AS cancellationPolicy,
                   b.canceled_at AS canceledAt,
                   b.number_of_adults AS numberOfAdults,
                   b.number_of_children AS numberOfChildren,
                   b.is_guaranteed AS isGuaranteed
            FROM bookings b
            LEFT JOIN customers c ON b.customer_id = c.id
            WHERE c.name LIKE %:customerName% AND b.hotel_id = :hotelId AND b.deleted = false
            ORDER BY COALESCE(b.check_in_date, b.booking_date) ASC
            """, nativeQuery = true)
    List<IBookingDto> searchByCustomerName(@Param("customerName") String customerName, @Param("hotelId") Integer hotelId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE bookings SET status = :status WHERE id = :bookingId", nativeQuery = true)
    void updateStatusBooking(@Param("bookingId") int bookingId, @Param("status") String status);
    @Modifying
    @Query("UPDATE Booking b SET b.confirmedTime = :confirmTime WHERE b.id = :bookingId")
    void confirm(@Param("bookingId") Integer bookingId, @Param("confirmTime") Long confirmTime);

    @Transactional
    @Modifying
    @Query(value = "UPDATE bookings SET check_in_time = :checkInTime WHERE id = :bookingId", nativeQuery = true)
    void checkin(@Param("bookingId") int bookingId, @Param("checkInTime") Long checkInTime);

    @Modifying
    @Query("UPDATE Booking b SET b.checkOutTime = :checkOutTime WHERE b.id = :bookingId")
    void checkout(@Param("bookingId") Integer bookingId, @Param("checkOutTime") Long checkOutTime);


    @Query(value = """
            SELECT b.id AS id, 
                   b.check_in_date AS checkInDate,
                   b.check_in_time AS checkInTime,
                   b.check_out_time AS checkOutTime,
                   b.check_out_date AS checkOutDate,
                   b.confirmed_time AS confirmedTime,
                   b.estimated_arrival_time AS estimatedArrivalTime,
                   b.booking_date AS bookingDate,
                   b.is_group AS isGroup,
                   b.total_cost AS totalCost,
                   b.status AS status,
                   b.deposit AS deposit,
                   b.cancellation_policy AS cancellationPolicy,
                   b.canceled_at AS canceledAt,
                   b.number_of_adults AS numberOfAdults,
                   b.number_of_children AS numberOfChildren,
                   b.is_guaranteed AS isGuaranteed
            FROM bookings b 
            WHERE b.id = :id
            """, nativeQuery = true)
    IBookingDto findBookingDtoById(@Param("id") Integer id);

    @Query(value = """
            SELECT b.id AS id, 
                   b.check_in_date AS checkInDate,
                   b.check_in_time AS checkInTime,
                   b.check_out_time AS checkOutTime,
                   b.check_out_date AS checkOutDate,
                   b.confirmed_time AS confirmedTime,
                   b.estimated_arrival_time AS estimatedArrivalTime,
                   b.booking_date AS bookingDate,
                   b.is_group AS isGroup,
                   b.total_cost AS totalCost,
                   b.status AS status,
                   b.deposit AS deposit,
                   b.cancellation_policy AS cancellationPolicy,
                   b.canceled_at AS canceledAt,
                   b.number_of_adults AS numberOfAdults,
                   b.number_of_children AS numberOfChildren,
                   b.is_guaranteed AS isGuaranteed
            FROM bookings b
            WHERE b.hotel_id = :hotelId AND b.deleted = false
            """, nativeQuery = true)
    List<IBookingDto> findAllBookingDtos(@Param("hotelId") Integer hotelId);

    List<Booking> findAllBookingsByHotelId(@Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT DISTINCT r.id AS id,
                   r.room_number AS roomNumber,
                   r.floor AS floor,
                   r.size AS size,
                   r.is_available AS isAvailable,
                   r.is_smoking_allowed AS isSmokingAllowed,
                   r.has_private_kitchen AS hasPrivateKitchen,
                   r.has_private_bathroom AS hasPrivateBathroom,
                   r.has_balcony AS hasBalcony,
                   r.has_lake_view AS hasLakeView,
                   r.has_garden_view AS hasGardenView,
                   r.has_pool_view AS hasPoolView,
                   r.has_mountain_view AS hasMountainView,
                   r.has_landmark_view AS hasLandmarkView,
                   r.has_city_view AS hasCityView,
                   r.has_river_view AS hasRiverView,
                   r.has_courtyard_view AS hasCourtyardView,
                   r.has_free_wifi AS hasFreeWifi,
                   r.has_soundproofing AS hasSoundproofing,
                   r.description AS description,
                   r.images AS images,
                   r.room_type_id AS roomTypeId
            FROM rooms r
            JOIN booking_rooms br ON r.id = br.room_id
            WHERE br.booking_id = :bookingId
            """, nativeQuery = true)
    List<IRoomDto> findAllRoomsByBookingId(@Param("bookingId") Integer bookingId);
    
}
