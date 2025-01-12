package ntu.service_centric.global_dorm.services;

import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.repositories.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getRoomById(int roomID) {
        return roomRepository.findFirstByRoomID(roomID); // Use unique query
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> updateRoom(int roomID, Room room) {
        return roomRepository.findFirstByRoomID(roomID).map(existingRoom -> {
            room.setId(existingRoom.getId()); // Retain MongoDB ObjectId
            return roomRepository.save(room);
        });
    }

    public boolean deleteRoom(int roomID) {
        return roomRepository.findFirstByRoomID(roomID).map(room -> {
            roomRepository.delete(room);
            return true;
        }).orElse(false); // Properly close the method
    }

    public Optional<Room> checkRoomAvailability(int roomID) {
        return roomRepository.findFirstByRoomID(roomID).filter(Room::getIsAvailable);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByIsAvailableTrue(); // Query to fetch only available rooms
    }
}
