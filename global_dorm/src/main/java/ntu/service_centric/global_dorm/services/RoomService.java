package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Retrieve all rooms.
     *
     * @return List of all rooms.
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Retrieve a room by its ID.
     *
     * @param id ID of the room.
     * @return Optional containing the room if found, or empty otherwise.
     */
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    /**
     * Save a new or updated room.
     *
     * @param room Room to save.
     * @return The saved room.
     */
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Update an existing room by its ID.
     *
     * @param id   ID of the room to update.
     * @param updatedRoom Room object with updated details.
     * @return Optional containing the updated room if the room exists, or empty otherwise.
     */
    public Optional<Room> updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(existingRoom -> {
            updatedRoom.setId(id); // Ensure the ID remains consistent
            return roomRepository.save(updatedRoom);
        });
    }

    /**
     * Delete a room by its ID.
     *
     * @param id ID of the room to delete.
     * @return true if the room was deleted, false if it did not exist.
     */
    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
