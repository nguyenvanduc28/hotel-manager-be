package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.auth.AuthLoginDto;
import hotelmanager.demo.dto.auth.AuthResponse;
import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.models.UserEntity;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.rooms.ConsumableService;
import hotelmanager.demo.services.rooms.EquipmentService;
import hotelmanager.demo.services.rooms.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final ConsumableService consumableService;
    private final EquipmentService equipmentService;
    private final RoomService roomService;

    @PostMapping("consumable-category")
    public ResponseEntity<ResponseObject> createConsumableCategory(
            @RequestBody @Valid ConsumableCategoryDto consumableCategoryDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        ConsumableCategoryDto categoryDto = consumableService.createConsumableCategory(consumableCategoryDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDto)
                .message("consumable ctegory created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("consumable")
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid ConsumableDto consumableDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        ConsumableDto consumableDtoN = consumableService.createConsumable(consumableDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableDtoN)
                .message("consumable created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("consumable-list")
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid List<ConsumableDto> consumableDtos,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        List<ConsumableDto> consumableDtoN = consumableService.createConsumableList(consumableDtos, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableDtoN)
                .message("all consumable created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("consumable")
    public ResponseEntity<ResponseObject> updateConsumable(
            @RequestBody @Valid ConsumableDto consumableDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        ConsumableDto updatedConsumable = consumableService.updateConsumable(consumableDto, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedConsumable)
                .message("Consumable updated successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable-category")
    public ResponseEntity<ResponseObject> getAllConsumableCategories(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<ConsumableCategoryDto> consumableCategories = consumableService.getAllConsumableCategories(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableCategories)
                .message("Fetched all consumable categories")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable")
    public ResponseEntity<ResponseObject> getAllConsumables(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<ConsumableDto> consumables = consumableService.getAllConsumables(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumables)
                .message("Fetched all consumables")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable/available")
    public ResponseEntity<ResponseObject> getAllConsumablesAvailable(@RequestParam Integer roomId, @AuthenticationPrincipal CustomUserDetails user) {
        List<ConsumableDto> consumables = consumableService.getAllConsumablesAvailable(roomId, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumables)
                .message("Fetched all consumables available of room")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable/{id}")
    public ResponseEntity<ResponseObject> getConsumableById(@PathVariable Integer id) {
        ConsumableDto consumable = consumableService.getConsumableById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumable)
                .message("Fetched consumable with ID: " + id)
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable/room/{roomId}")
    public ResponseEntity<ResponseObject> getAllConsumablesByRoomId(@PathVariable Integer roomId) {
        List<ConsumableDto> consumables = consumableService.getAllConsumablesByRoomId(roomId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumables)
                .message("Fetched all consumables by room id")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("equipment/room/{roomId}")
    public ResponseEntity<ResponseObject> getAllEquipmentByRoomId(@PathVariable Integer roomId) {
        List<EquipmentDto> equipments = equipmentService.getAllEquipmentByRoomId(roomId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipments)
                .message("Fetched all equipment by room id")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("equipment-category")
    public ResponseEntity<ResponseObject> createEquipmentCategory(
            @RequestBody @Valid EquipmentCategoryDto equipmentCategoryDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        EquipmentCategoryDto equipmentCategoryDto1 = equipmentService.createEquipmentCategory(equipmentCategoryDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentCategoryDto1)
                .message("equipment category created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("equipment")
    public ResponseEntity<ResponseObject> createEquipment(
            @RequestBody @Valid EquipmentDto equipmentDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        EquipmentDto equipmentDto1 = equipmentService.createEquipment(equipmentDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentDto1)
                .message("equipment created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("equipment-list")
    public ResponseEntity<ResponseObject> createEquipmentList(
            @RequestBody @Valid List<EquipmentDto> equipmentDtos,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        List<EquipmentDto> equipmentDtos1 = equipmentService.createEquipmentList(equipmentDtos, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentDtos1)
                .message("all equipment created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("equipment")
    public ResponseEntity<ResponseObject> updateEquipment(
            @RequestBody @Valid EquipmentDto equipmentDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        EquipmentDto updatedEquipment = equipmentService.updateEquipment(equipmentDto, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedEquipment)
                .message("Equipment updated successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("equipment/{id}")
    public ResponseEntity<ResponseObject> getEquipmentById(@PathVariable Integer id) {
        EquipmentDto equipmentDto = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentDto)
                .message("Fetched equipment with ID: " + id)
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("roomtype")
    public ResponseEntity<ResponseObject> createRoomType(
            @RequestBody @Valid RoomTypeDto roomTypeDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        RoomTypeDto roomTypeDto1 = roomService.createRoomType(roomTypeDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeDto1)
                .message("roomtype created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("roomtype")
    public ResponseEntity<ResponseObject> getAllRoomType(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<RoomTypeDto> roomTypeDtos = roomService.getAllRoomTypes(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeDtos)
                .message("Fetched all roomtype")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("create")
    public ResponseEntity<ResponseObject> createRoom(
            @RequestBody @Valid RoomDto roomDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        RoomDto roomDto1 = roomService.createRoom(roomDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDto1)
                .message("room created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateRoom(
            @RequestBody @Valid RoomDto roomDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        RoomDto roomDto1 = roomService.updateRoom(roomDto, user.getUser().getHotelId());

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDto1)
                .message("room updated")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllRoom(@AuthenticationPrincipal CustomUserDetails user) {
        List<RoomDto> roomDtos = roomService.getAllRooms(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDtos)
                .message("Fetched all rooms for hotel")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getRoomById(@PathVariable Integer id) {
        RoomDto roomDtos = roomService.getRoomById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDtos)
                .message("Fetched room by id")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("equipment-category")
    public ResponseEntity<ResponseObject> getAllEquipmentCategories(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<EquipmentCategoryDto> equipmentCategories = equipmentService.getAllEquipmentCategories(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentCategories)
                .message("Fetched all equipment categories")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("equipment")
    public ResponseEntity<ResponseObject> getAllEquipment(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<EquipmentDto> equipmentList = equipmentService.getAllEquipment(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentList)
                .message("Fetched all equipment")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("equipment/available")
    public ResponseEntity<ResponseObject> getAllEquipmentAvailable(@RequestParam Integer roomId, @AuthenticationPrincipal CustomUserDetails user) {
        List<EquipmentDto> equipmentList = equipmentService.getAllEquipmentAvailable(roomId, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentList)
                .message("Fetched all equipment available of room")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("roomtype/price")
    public ResponseEntity<ResponseObject> getAllRoomTypesWithPriceInRange(@RequestParam Long checkInDate, @RequestParam Long checkOutDate, @AuthenticationPrincipal CustomUserDetails user) {
        List<RoomTypeDto> roomTypeDtos = roomService.getAllRoomTypesWithPriceInRange(checkInDate, checkOutDate, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeDtos)
                .message("Fetched all room types with price in range")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("roomtype/price")
    public ResponseEntity<ResponseObject> updatePriceRoomType(@RequestBody RoomPriceDto roomPriceDto) {
        RoomPriceDto roomPriceDtos1 = roomService.updatePriceRoomType(roomPriceDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomPriceDtos1)
                .message("Price updated successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

}
