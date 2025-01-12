package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.Application;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    Optional<Application> findByAppID(int appID);

    List<Application> findByUserID(int userID);

    List<Application> findByRoomID(int roomID);

    Optional<Application> findFirstByOrderByAppIDDesc(); // ChatGPT
}
