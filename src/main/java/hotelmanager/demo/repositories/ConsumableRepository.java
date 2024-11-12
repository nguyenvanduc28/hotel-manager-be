package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.roomDtos.IConsumableDto;
import hotelmanager.demo.models.Consumable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ConsumableRepository extends JpaRepository<Consumable, Integer> {
    @Query(value = "SELECT c.id AS id, c.name AS name, c.room_id AS roomId, \n" +
            "       c.consumable_category_id AS consumableCategoryId, \n" +
            "       c.price AS price, c.quantity AS quantity, c.unit AS unit, \n" +
            "       c.expiry_date AS expiryDate, c.barcode AS barcode, \n" +
            "       c.description AS description\n" +
            "FROM consumables c\n" +
            "WHERE c.room_id = :roomId", nativeQuery = true)
    List<IConsumableDto> findConsumablesByRoomId(@Param("roomId") Integer roomId);

    @Query(value = "SELECT c.id AS id, c.name AS name, c.room_id AS roomId, \n" +
            "       c.consumable_category_id AS consumableCategoryId, \n" +
            "       c.price AS price, c.quantity AS quantity, c.unit AS unit, \n" +
            "       c.expiry_date AS expiryDate, c.barcode AS barcode, \n" +
            "       c.description AS description\n" +
            "FROM consumables c\n", nativeQuery = true)
    List<IConsumableDto> findAllConsumables();

    @Query(value = "SELECT c.id AS id, c.name AS name, c.room_id AS roomId, \n" +
            "       c.consumable_category_id AS consumableCategoryId, \n" +
            "       c.price AS price, c.quantity AS quantity, c.unit AS unit, \n" +
            "       c.expiry_date AS expiryDate, c.barcode AS barcode, \n" +
            "       c.description AS description\n" +
            "FROM consumables c WHERE c.room_id =:roomId OR c.room_id IS NULL\n", nativeQuery = true)
    List<IConsumableDto> findAllConsumablesAvailable(@Param("roomId") Integer roomId);

    @Query(value = "SELECT c.id AS id, c.name AS name, c.room_id AS roomId, \n" +
            "       c.consumable_category_id AS consumableCategoryId, \n" +
            "       c.price AS price, c.quantity AS quantity, c.unit AS unit, \n" +
            "       c.expiry_date AS expiryDate, c.barcode AS barcode, \n" +
            "       c.description AS description\n" +
            "FROM consumables c WHERE c.room_id =:roomId\n", nativeQuery = true)
    List<IConsumableDto> findAllConsumablesByRoomId(@Param("roomId") Integer roomId);

    @Query(value = "SELECT c.id AS id, c.name AS name, c.room_id AS roomId, \n" +
            "       c.consumable_category_id AS consumableCategoryId, \n" +
            "       c.price AS price, c.quantity AS quantity, c.unit AS unit, \n" +
            "       c.expiry_date AS expiryDate, c.barcode AS barcode, \n" +
            "       c.description AS description\n" +
            "FROM consumables c  WHERE c.id =:id\n", nativeQuery = true)
    IConsumableDto findConsumableById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE consumables SET room_id = :roomId WHERE id = :consumableId", nativeQuery = true)
    void updateRoomId(@Param("consumableId") int consumableId, @Param("roomId") Integer roomId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE consumables SET quantity = :quantity WHERE id = :consumableId", nativeQuery = true)
    void updateQuantity(@Param("consumableId") int consumableId, @Param("quantity") Integer quantity);

    
}
