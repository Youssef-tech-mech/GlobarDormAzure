package ntu.service_centric.global_dorm.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "applications")
public class Application {

    @Id
    private ObjectId id; // MongoDB ObjectId for unique application ID

    @NotNull(message = "Application ID is required.")
    private int appID; // Auto-generated, unique application ID

    @NotNull(message = "User ID is required.")
    private int userID; // Unique user ID

    @NotNull(message = "Room ID is required.")
    private int roomID; // Room ID

    @NotNull(message = "Application date must be provided.")
    private LocalDateTime applicationDate; // Date of the application

    @NotBlank(message = "Status is required.")
    private String status; // Status of the application (e.g., Pending, Approved, Denied)
}
