package ntu.service_centric.global_dorm.repositories;

import ntu.service_centric.global_dorm.models.Credential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CredentialRepository extends MongoRepository<Credential, String> {

    Optional<Credential> findByUsername(String username);
}
