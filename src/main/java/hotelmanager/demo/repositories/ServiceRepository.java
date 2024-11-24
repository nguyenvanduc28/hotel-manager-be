package hotelmanager.demo.repositories;

import hotelmanager.demo.models.ServiceHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceHotel, Integer> {
    List<ServiceHotel> findAllByHotelId(int hotelId);
    
}
