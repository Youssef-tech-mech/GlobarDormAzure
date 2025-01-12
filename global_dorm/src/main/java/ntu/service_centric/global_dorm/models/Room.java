package ntu.service_centric.global_dorm.models;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

/**
 * Represents a room in the Global Dorm application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
public class Room {

    @Id
    private String id; // MongoDB's default ObjectId (used as the database primary key)

    @NotNull(message = "Room ID must be provided.")
    @Positive(message = "Room ID must be a positive integer.")
    private int roomID; // Logical identifier for the application

    @NotBlank(message = "Room name is required.")
    private String name;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "County is required.")
    private String county;

    @NotBlank(message = "Postcode is required.")
    private String postcode;

    @NotNull(message = "Furnished status must be specified.")
    private Boolean furnished;

    @NotEmpty(message = "Amenities must be provided.")
    private List<String> amenities;

    @NotNull(message = "Live-in landlord status must be specified.")
    private Boolean liveInLandlord;

    @PositiveOrZero(message = "Number of people shared with must be zero or more.")
    private int sharedWith;

    @NotNull(message = "Bills included status must be specified.")
    private Boolean billsIncluded;

    @NotNull(message = "Bathroom shared status must be specified.")
    private Boolean bathroomShared;

    @Positive(message = "Price per month must be greater than zero.")
    private double pricePerMonthGbp;

    @NotBlank(message = "Availability date is required.")
    private String availabilityDate;

    @NotEmpty(message = "Spoken languages must be specified.")
    private Set<String> spokenLanguages;

    @Field("availability")
    @NotNull(message = "Availability must be specified.")
    private Boolean isAvailable;

    @NotNull(message = "Location must be provided.")
    private Location location; // New location field for coordinates

    /**
     * Represents geographical coordinates for the room's location.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        @NotNull(message = "Latitude must be provided.")
        @Min(value = -90, message = "Latitude must be between -90 and 90.")
        @Max(value = 90, message = "Latitude must be between -90 and 90.")
        private Double latitude;

        @NotNull(message = "Longitude must be provided.")
        @Min(value = -180, message = "Longitude must be between -180 and 180.")
        @Max(value = 180, message = "Longitude must be between -180 and 180.")
        private Double longitude;
    }
}
