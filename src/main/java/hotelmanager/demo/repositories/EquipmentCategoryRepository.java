package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.roomDtos.IEquipmentCategoryDto;
import hotelmanager.demo.models.room.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Integer> {
    @Query(value = "SELECT c.id AS id, c.name AS name, c.description AS description " +
            "FROM equipment_categories c " +
            "WHERE c.id = :id and c.deleted = false", nativeQuery = true)
    IEquipmentCategoryDto findCategoryById(@Param("id") Integer id);

    @Query(value = "SELECT c.id AS id, c.name AS name, c.description AS description " +
            "FROM equipment_categories c " +
            "WHERE c.hotel_id = :hotelId and c.deleted = false", nativeQuery = true)
    List<IEquipmentCategoryDto> findAllEquipCateByHotelId(@Param("hotelId") Integer hotelId);
    
}
