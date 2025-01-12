package ntu.service_centric.global_dorm.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private ObjectId id; // MongoDB ObjectId

    @NotNull(message = "User ID is required.")
    private int userID; // Unique numeric ID for the user

    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Role is required.")
    private String role; // Admin or User
}
