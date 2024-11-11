package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.roomDtos.IEquipmentDto;
import hotelmanager.demo.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    @Query(value = "SELECT e.id AS id, e.name AS name, e.room_id AS roomId, \n" +
            "       e.equipment_category_id AS equipmentCategoryId, \n" +
            "       e.installation_date AS installationDate, e.barcode AS barcode, \n" +
            "       e.status AS status, e.description AS description\n" +
            "FROM equipment e\n" +
            "WHERE e.room_id = :roomId", nativeQuery = true)
    List<IEquipmentDto> findEquipmentByRoomId(@Param("roomId") Integer roomId);

    @Query(value = "SELECT e.id AS id, e.name AS name, e.room_id AS roomId, \n" +
            "       e.equipment_category_id AS equipmentCategoryId, \n" +
            "       e.installation_date AS installationDate, e.barcode AS barcode, \n" +
            "       e.status AS status, e.description AS description\n" +
            "FROM equipment e\n", nativeQuery = true)
    List<IEquipmentDto> findAllEquipment();
    @Query(value = "SELECT e.id AS id, e.name AS name, e.room_id AS roomId, \n" +
            "       e.equipment_category_id AS equipmentCategoryId, \n" +
            "       e.installation_date AS installationDate, e.barcode AS barcode, \n" +
            "       e.status AS status, e.description AS description\n" +
            "FROM equipment e WHERE e.room_id = :roomId OR e.room_id IS NULL\n", nativeQuery = true)
    List<IEquipmentDto> findAllEquipmentAvalable(@Param("roomId") Integer roomId);

    @Query(value = "SELECT e.id AS id, e.name AS name, e.room_id AS roomId, \n" +
            "       e.equipment_category_id AS equipmentCategoryId, \n" +
            "       e.installation_date AS installationDate, e.barcode AS barcode, \n" +
            "       e.status AS status, e.description AS description\n" +
            "FROM equipment e WHERE e.room_id = :roomId\n", nativeQuery = true)
    List<IEquipmentDto> findAllEquipmentByRoomId(@Param("roomId") Integer roomId);

    @Query(value = "SELECT e.id AS id, e.name AS name, e.room_id AS roomId, \n" +
            "       e.equipment_category_id AS equipmentCategoryId, \n" +
            "       e.installation_date AS installationDate, e.barcode AS barcode, \n" +
            "       e.status AS status, e.description AS description\n" +
            "FROM equipment e WHERE e.id =:id\n", nativeQuery = true)
    IEquipmentDto findEquipmentById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE equipment SET room_id = :roomId WHERE id = :equipmentId", nativeQuery = true)
    void updateRoomId(@Param("equipmentId") int equipmentId, @Param("roomId") Integer roomId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE equipment SET status = :status WHERE id = :equipmentId", nativeQuery = true)
    void updateStatus(@Param("equipmentId") int equipmentId, @Param("status") String status);

}
