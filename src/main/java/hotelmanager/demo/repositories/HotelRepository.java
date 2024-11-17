package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    // viết native query
    @Query(value = "SELECT h.* FROM hotel h INNER JOIN user u ON h.id = u.hotel_id WHERE u.id = :userId", nativeQuery = true)
    Hotel findByUserId(@Param("userId") Integer userId);
    @Query(value = "SELECT DISTINCT h.* FROM hotel h WHERE LOWER(address) LIKE LOWER(CONCAT('%', :location, '%')) OR LOWER(city) LIKE LOWER(CONCAT('%', :location, '%'))", nativeQuery = true)
    List<Hotel> findByAddressOrCityContainingIgnoreCase(@Param("location") String location);
}
