package hotelmanager.demo.repositories;

import hotelmanager.demo.models.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, Integer> {
}
