package hotelmanager.demo.controllers;

import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.dto.auth.AuthLoginDto;
import hotelmanager.demo.dto.auth.AuthResponse;
import hotelmanager.demo.dto.roomDtos.*;
import hotelmanager.demo.services.rooms.ConsumableService;
import hotelmanager.demo.services.rooms.EquipmentService;
import hotelmanager.demo.services.rooms.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final ConsumableService consumableService;
    private final EquipmentService equipmentService;
    private final RoomService roomService;

    @PostMapping("consumable-category")
    public ResponseEntity<ResponseObject> createConsumableCatrgory(
            @RequestBody @Valid ConsumableCategoryDto consumableCategoryDto
    ) {

        ConsumableCategoryDto categoryDto = consumableService.createConsumableCategory(consumableCategoryDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDto)
                .message("consumable ctegory created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("consumable")
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid ConsumableDto consumableDto
    ) {

        ConsumableDto consumableDtoN = consumableService.createConsumable(consumableDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableDtoN)
                .message("consumable created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("consumable-list")
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid List<ConsumableDto> consumableDtos
    ) {

        List<ConsumableDto> consumableDtoN = consumableService.createConsumableList(consumableDtos);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableDtoN)
                .message("all consumable created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("consumable")
    public ResponseEntity<ResponseObject> updateConsumable(
            @RequestBody @Valid ConsumableDto consumableDto
    ) {
        ConsumableDto updatedConsumable = consumableService.updateConsumable(consumableDto);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedConsumable)
                .message("Consumable updated successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable-category")
    public ResponseEntity<ResponseObject> getAllConsumableCategories() {
        List<ConsumableCategoryDto> consumableCategories = consumableService.getAllConsumableCategories();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumableCategories)
                .message("Fetched all consumable categories")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable")
    public ResponseEntity<ResponseObject> getAllConsumables() {
        List<ConsumableDto> consumables = consumableService.getAllConsumables();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(consumables)
                .message("Fetched all consumables")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("consumable/available")
    public ResponseEntity<ResponseObject> getAllConsumablesAvailable(@RequestParam Integer roomId) {
        List<ConsumableDto> consumables = consumableService.getAllConsumablesAvailable(roomId);
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

    @PostMapping("equipment-category")
    public ResponseEntity<ResponseObject> createEquipmentCatrgory(
            @RequestBody @Valid EquipmentCategoryDto equipmentCategoryDto
    ) {

        EquipmentCategoryDto equipmentCategoryDto1 = equipmentService.createEquipmentCategory(equipmentCategoryDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentCategoryDto1)
                .message("equipment category created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("equipment")
    public ResponseEntity<ResponseObject> createEquipment(
            @RequestBody @Valid EquipmentDto equipmentDto
    ) {

        EquipmentDto equipmentDto1 = equipmentService.createEquipment(equipmentDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentDto1)
                .message("equipment created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PostMapping("equipment-list")
    public ResponseEntity<ResponseObject> createEquipmentList(
            @RequestBody @Valid List<EquipmentDto> equipmentDtos
    ) {

        List<EquipmentDto> equipmentDtos1 = equipmentService.createEquipmentList(equipmentDtos);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentDtos1)
                .message("all equipment created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("equipment")
    public ResponseEntity<ResponseObject> updateEquipment(
            @RequestBody @Valid EquipmentDto equipmentDto
    ) {
        EquipmentDto updatedEquipment = equipmentService.updateEquipment(equipmentDto);
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
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid RoomTypeDto roomTypeDto
    ) {
        RoomTypeDto roomTypeDto1 = roomService.createRoomType(roomTypeDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeDto1)
                .message("roomtype created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("roomtype")
    public ResponseEntity<ResponseObject> getAllRoomType() {
        List<RoomTypeDto> roomTypeDtos = roomService.getAllRoomTypes();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomTypeDtos)
                .message("Fetched all roomtype")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("create")
    public ResponseEntity<ResponseObject> createConsumable(
            @RequestBody @Valid RoomDto roomDto
    ) {
        RoomDto roomDto1 = roomService.createRoom(roomDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDto1)
                .message("room created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("update")
    public ResponseEntity<ResponseObject> updateRoom(
            @RequestBody @Valid RoomDto roomDto
    ) {
        RoomDto roomDto1 = roomService.updateRoom(roomDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDto1)
                .message("room updated")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllRoom() {
        List<RoomDto> roomDtos = roomService.getAllRooms();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDtos)
                .message("Fetched all room")
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
    public ResponseEntity<ResponseObject> getAllEquipmentCategories() {
        List<EquipmentCategoryDto> equipmentCategories = equipmentService.getAllEquipmentCategories();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentCategories)
                .message("Fetched all equipment categories")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("available")
    public ResponseEntity<ResponseObject> getAvailableRooms(
            @RequestParam Long checkInDate,
            @RequestParam Long checkOutDate) {
        List<RoomDto> roomDtos = roomService.getAvailableRooms(checkInDate, checkOutDate);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(roomDtos)
                .message("Fetched available rooms")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("equipment")
    public ResponseEntity<ResponseObject> getAllEquipment() {
        List<EquipmentDto> equipmentList = equipmentService.getAllEquipment();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentList)
                .message("Fetched all equipment")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("equipment/available")
    public ResponseEntity<ResponseObject> getAllEquipmentAvailable(@RequestParam Integer roomId) {
        List<EquipmentDto> equipmentList = equipmentService.getAllEquipmentAvailable(roomId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(equipmentList)
                .message("Fetched all equipment available of room")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

}
