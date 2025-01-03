package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Room entities.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
