package hotelmanager.demo.repositories;

import hotelmanager.demo.models.booking.BookingServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingServiceRepository extends JpaRepository<BookingServiceEntity, Integer> {
    List<BookingServiceEntity> findAllServiceItemByBookingId(Integer bookingId);
    void deleteAllByBookingId(Integer bookingId);

    BookingServiceEntity findByBookingId(Integer bookingId);
}
