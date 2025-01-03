package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Retrieve all rooms.
     *
     * @return List of rooms.
     */
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    /**
     * Retrieve a room by its ID.
     *
     * @param id Room ID.
     * @return Room if found, otherwise 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        return room.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new room.
     *
     * @param room Room object to create.
     * @return Created Room with 201 Created status.
     */
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.saveRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    /**
     * Update an existing room.
     *
     * @param id   Room ID to update.
     * @param room Updated Room object.
     * @return Updated Room if found, otherwise 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        Optional<Room> updatedRoom = roomService.updateRoom(id, room);
        return updatedRoom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a room by its ID.
     *
     * @param id Room ID to delete.
     * @return 204 No Content if deleted, otherwise 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        boolean deleted = roomService.deleteRoom(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
