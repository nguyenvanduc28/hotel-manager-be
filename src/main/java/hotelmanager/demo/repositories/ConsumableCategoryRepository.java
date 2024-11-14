package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.roomDtos.IConsumableCategoryDto;
import hotelmanager.demo.models.ConsumableCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumableCategoryRepository extends JpaRepository<ConsumableCategory, Integer> {
    @Query(value = "SELECT c.id AS id, c.name AS name, c.description AS description " +
            "FROM consumable_categories c " +
            "WHERE c.id = :id AND c.deleted = false", nativeQuery = true)
    IConsumableCategoryDto findCategoryById(@Param("id") Integer id);
    @Query(value = "SELECT c.id AS id, c.name AS name, c.description AS description " +
            "FROM consumable_categories c " +
            "WHERE c.hotel_id = :hotelId AND c.deleted = false", nativeQuery = true)
    List<IConsumableCategoryDto> findAllConCategoriesByHotelId(@Param("hotelId") Integer hotelId);

}
