package ntu.service_centric.global_dorm.controllers;

import jakarta.validation.Valid;
import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Return all rooms as JSON
    @GetMapping("/data")
    public ResponseEntity<List<Room>> getAllRoomsAsJson() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Render rooms.html for room data
    @GetMapping("/all")
    public ResponseEntity<String> showRoomsPage() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/rooms.html");
        String htmlContent = new String(Files.readAllBytes(resource.getFile().toPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }

    // Get room by ID
    @GetMapping("/{roomID}")
    public ResponseEntity<Room> getRoomById(@PathVariable int roomID) {
        return roomService.getRoomById(roomID)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new room
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody @Valid Room room) {
        if (roomService.getRoomById(room.getRoomID()).isPresent()) {
            return ResponseEntity.badRequest().body(null); // Duplicate roomID
        }
        return ResponseEntity.ok(roomService.createRoom(room));
    }

    // Update an existing room
    @PutMapping("/{roomID}")
    public ResponseEntity<Room> updateRoom(@PathVariable int roomID, @Valid @RequestBody Room room) {
        if (room.getLocation() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<Room> updatedRoom = roomService.updateRoom(roomID, room);
        return updatedRoom.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Delete a room by ID
    @DeleteMapping("/{roomID}")
    public ResponseEntity<String> deleteRoom(@PathVariable int roomID) {
        boolean deleted = roomService.deleteRoom(roomID);
        if (deleted) {
            return ResponseEntity.ok("Room with ID " + roomID + " has been deleted.");
        }
        return ResponseEntity.notFound().build();
    }

    // Check availability of a room
    @GetMapping("/{roomID}/availability")
    public ResponseEntity<String> checkRoomAvailability(@PathVariable int roomID) {
        Optional<Room> room = roomService.checkRoomAvailability(roomID);
        if (room.isPresent()) {
            return ResponseEntity.ok("Room with ID " + roomID + " is available.");
        }
        return ResponseEntity.ok("Room with ID " + roomID + " is not available.");
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(availableRooms);

    }

}
