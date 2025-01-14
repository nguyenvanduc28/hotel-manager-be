package hotelmanager.demo.repositories;

import hotelmanager.demo.models.room.RoomType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
    List<RoomType> findAllByHotelId(Integer hotelId);
}
