package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserID(int userID);

    Optional<User> findByEmail(String email);
}
