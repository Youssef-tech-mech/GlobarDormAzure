package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {

    // Find the first room by its logical roomID (to avoid non-unique results)
    Optional<Room> findFirstByRoomID(int roomID);

    // Find all rooms by city
    List<Room> findByCity(String city);

    // Find all available rooms
    List<Room> findByIsAvailableTrue();

    // Find a specific room by roomID and availability
    Optional<Room> findByRoomIDAndIsAvailable(int roomID, Boolean isAvailable);
}
