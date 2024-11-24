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

    List<BookingServiceOrder> findByHotelIdAndStatus(Integer hotelId, String status);

    } 
