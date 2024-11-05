package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.roomDtos.IEquipmentCategoryDto;
import hotelmanager.demo.models.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Integer> {
    @Query(value = "SELECT c.id AS id, c.name AS name, c.description AS description " +
            "FROM equipment_categories c " +
            "WHERE c.id = :id", nativeQuery = true)
    IEquipmentCategoryDto findCategoryById(@Param("id") Integer id);
}
