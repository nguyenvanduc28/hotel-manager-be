package hotelmanager.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import hotelmanager.demo.models.BookingServiceOrder;

@Repository
public interface BookingServiceOrderRepository extends JpaRepository<BookingServiceOrder, Integer> {
    List<BookingServiceOrder> findByBookingServiceId(Integer bookingServiceId);

    @Modifying
    @Query("UPDATE BookingServiceOrder bso SET bso.status = :status WHERE bso.id = :orderId")
    void confirmServicedForServiceOrder(@Param("orderId") Integer orderId, @Param("status") String status);

    @Query("SELECT COUNT(bso) FROM BookingServiceOrder bso WHERE bso.status = :status AND bso.hotelId = :hotelId")
    int countByStatusAndHotelId(@Param("status") String status, @Param("hotelId") Integer hotelId);

    List<BookingServiceOrder> findByHotelIdAndStatus(Integer hotelId, String status);

    List<BookingServiceOrder> findAllByStatusAndHotelIdAndServiceTypeId(String status, Integer hotelId, Integer serviceTypeId);

    @Modifying
    @Query("UPDATE BookingServiceOrder bso SET bso.status = :status WHERE bso.id = :orderId")
    void updateStatus(@Param("orderId") Integer orderId, @Param("status") String status);

    @Query("SELECT bso FROM BookingServiceOrder bso WHERE bso.hotelId = :hotelId AND bso.status != 'Đã phục vụ' AND bso.serviceTypeId = :serviceTypeId")
    List<BookingServiceOrder> findAllOrderAvailableWithServiceTypeId(@Param("hotelId") Integer hotelId, @Param("serviceTypeId") Integer serviceTypeId);

    @Query(value = "SELECT * FROM booking_service_order bso WHERE bso.hotel_id = :hotelId AND bso.service_type_id = :serviceTypeId AND bso.order_created_at BETWEEN :startTime AND :endTime",
        nativeQuery = true)
    List<BookingServiceOrder> findAllByHotelIdAndServiceTypeIdAndOrderCreatedAtBetween(@Param("hotelId") Integer hotelId, @Param("serviceTypeId") Integer serviceTypeId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

} 
