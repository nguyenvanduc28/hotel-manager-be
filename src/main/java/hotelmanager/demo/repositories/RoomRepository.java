package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.INumOfHotelSearch;
import hotelmanager.demo.dto.roomDtos.IRoomDto;
import hotelmanager.demo.models.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = """
            SELECT DISTINCT r.id AS id,
                   r.room_number AS roomNumber,
                   r.floor AS floor,
                   r.size AS size,
                   r.is_available AS isAvailable,
                   r.is_smoking_allowed AS isSmokingAllowed,
                   r.has_private_kitchen AS hasPrivateKitchen,
                   r.has_private_bathroom AS hasPrivateBathroom,
                   r.has_balcony AS hasBalcony,
                   r.has_lake_view AS hasLakeView,
                   r.has_garden_view AS hasGardenView,
                   r.has_pool_view AS hasPoolView,
                   r.has_mountain_view AS hasMountainView,
                   r.has_landmark_view AS hasLandmarkView,
                   r.has_city_view AS hasCityView,
                   r.has_river_view AS hasRiverView,
                   r.has_courtyard_view AS hasCourtyardView,
                   r.has_free_wifi AS hasFreeWifi,
                   r.has_soundproofing AS hasSoundproofing,
                   r.description AS description,
                   r.images AS images,
                   r.room_type_id AS roomTypeId
            FROM rooms r
            WHERE r.id NOT IN (
                SELECT DISTINCT r2.id
                FROM booking_rooms br
                LEFT JOIN rooms r2 ON r2.id = br.room_id
                LEFT JOIN bookings b ON br.booking_id = b.id
                WHERE b.status IN ('Đang chờ', 'Đã xác nhận')
                      AND (b.check_in_date <= :checkOutDate AND b.check_out_date >= :checkInDate)
            )
            AND r.hotel_id = :hotelId 
            AND r.deleted = false
            """,
            nativeQuery = true)
    List<IRoomDto> findAvailableRooms(@Param("checkInDate") Long checkInDate,
                                      @Param("checkOutDate") Long checkOutDate,
                                      @Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT COUNT(DISTINCT r.id) AS count, MIN(rt.base_price_per_night) AS lowestPrice
            FROM rooms r
            LEFT JOIN room_types rt ON r.room_type_id = rt.id
            WHERE r.id NOT IN (
                SELECT DISTINCT r2.id
                FROM booking_rooms br
                LEFT JOIN rooms r2 ON r2.id = br.room_id
                LEFT JOIN bookings b ON br.booking_id = b.id
                WHERE b.status IN ('Đang chờ', 'Đã xác nhận')
                      AND (b.check_in_date <= :checkOutDate AND b.check_out_date >= :checkInDate)
            )
            AND r.hotel_id = :hotelId 
            AND r.deleted = false
            """,
            nativeQuery = true)
    INumOfHotelSearch getAvailableRoomCount(@Param("checkInDate") Long checkInDate,
                                            @Param("checkOutDate") Long checkOutDate,
                                            @Param("hotelId") Integer hotelId);

    @Query(value = "SELECT DISTINCT r.id AS id,\n" +
            "       r.room_number AS roomNumber,\n" +
            "       r.floor AS floor,\n" +
            "       r.size AS size,\n" +
            "       r.is_available AS isAvailable,\n" +
            "       r.is_smoking_allowed AS isSmokingAllowed,\n" +
            "       r.has_private_kitchen AS hasPrivateKitchen,\n" +
            "       r.has_private_bathroom AS hasPrivateBathroom,\n" +
            "       r.has_balcony AS hasBalcony,\n" +
            "       r.has_lake_view AS hasLakeView,\n" +
            "       r.has_garden_view AS hasGardenView,\n" +
            "       r.has_pool_view AS hasPoolView,\n" +
            "       r.has_mountain_view AS hasMountainView,\n" +
            "       r.has_landmark_view AS hasLandmarkView,\n" +
            "       r.has_city_view AS hasCityView,\n" +
            "       r.has_river_view AS hasRiverView,\n" +
            "       r.has_courtyard_view AS hasCourtyardView,\n" +
            "       r.has_free_wifi AS hasFreeWifi,\n" +
            "       r.has_soundproofing AS hasSoundproofing,\n" +
            "       r.description AS description,\n" +
            "       r.images AS images,\n" +
            "       r.room_type_id AS roomTypeId\n" +
            "FROM rooms r\n" +
            "WHERE r.hotel_id = :hotelId and r.deleted = false", nativeQuery = true)
    List<IRoomDto> findAllRooms(@Param("hotelId") Integer hotelId);

    @Query(value = "SELECT DISTINCT r.id AS id,\n" +
            "       r.room_number AS roomNumber,\n" +
            "       r.floor AS floor,\n" +
            "       r.size AS size,\n" +
            "       r.is_available AS isAvailable,\n" +
            "       r.is_smoking_allowed AS isSmokingAllowed,\n" +
            "       r.has_private_kitchen AS hasPrivateKitchen,\n" +
            "       r.has_private_bathroom AS hasPrivateBathroom,\n" +
            "       r.has_balcony AS hasBalcony,\n" +
            "       r.has_lake_view AS hasLakeView,\n" +
            "       r.has_garden_view AS hasGardenView,\n" +
            "       r.has_pool_view AS hasPoolView,\n" +
            "       r.has_mountain_view AS hasMountainView,\n" +
            "       r.has_landmark_view AS hasLandmarkView,\n" +
            "       r.has_city_view AS hasCityView,\n" +
            "       r.has_river_view AS hasRiverView,\n" +
            "       r.has_courtyard_view AS hasCourtyardView,\n" +
            "       r.has_free_wifi AS hasFreeWifi,\n" +
            "       r.has_soundproofing AS hasSoundproofing,\n" +
            "       r.description AS description,\n" +
            "       r.images AS images,\n" +
            "       r.room_type_id AS roomTypeId\n" +
            "FROM rooms r WHERE r.id = :roomId AND r.deleted = false",
            nativeQuery = true)
    IRoomDto findRoomById(@Param("roomId") Integer roomId);

    @Query(value = "SELECT DISTINCT r.id AS id,\n" +
            "       r.room_number AS roomNumber,\n" +
            "       r.floor AS floor,\n" +
            "       r.size AS size,\n" +
            "       r.is_available AS isAvailable,\n" +
            "       r.is_smoking_allowed AS isSmokingAllowed,\n" +
            "       r.has_private_kitchen AS hasPrivateKitchen,\n" +
            "       r.has_private_bathroom AS hasPrivateBathroom,\n" +
            "       r.has_balcony AS hasBalcony,\n" +
            "       r.has_lake_view AS hasLakeView,\n" +
            "       r.has_garden_view AS hasGardenView,\n" +
            "       r.has_pool_view AS hasPoolView,\n" +
            "       r.has_mountain_view AS hasMountainView,\n" +
            "       r.has_landmark_view AS hasLandmarkView,\n" +
            "       r.has_city_view AS hasCityView,\n" +
            "       r.has_river_view AS hasRiverView,\n" +
            "       r.has_courtyard_view AS hasCourtyardView,\n" +
            "       r.has_free_wifi AS hasFreeWifi,\n" +
            "       r.has_soundproofing AS hasSoundproofing,\n" +
            "       r.description AS description,\n" +
            "       r.images AS images,\n" +
            "       r.room_type_id AS roomTypeId\n" +
            "FROM rooms r WHERE r.id = :roomId AND r.hotel_id = :hotelId AND r.deleted = false",
            nativeQuery = true)
    IRoomDto findRoomByIdAndHotelId(@Param("roomId") Integer roomId, @Param("hotelId") Integer hotelId);

}
