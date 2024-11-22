package hotelmanager.demo.repositories;

import hotelmanager.demo.models.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Integer> {
    List<ServiceItem> findAllByHotelIdAndServiceTypeId(int hotelId, int serviceTypeId);
}
