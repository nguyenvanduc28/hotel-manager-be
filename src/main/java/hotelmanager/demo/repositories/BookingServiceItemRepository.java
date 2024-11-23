package hotelmanager.demo.repositories;

import hotelmanager.demo.models.BookingServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingServiceItemRepository extends JpaRepository<BookingServiceItem, Integer> {
    List<BookingServiceItem> findAllServiceItemByBookingId(Integer bookingId);
    void deleteAllByBookingId(Integer bookingId);
}
