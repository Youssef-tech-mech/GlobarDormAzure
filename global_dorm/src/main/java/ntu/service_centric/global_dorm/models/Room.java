package ntu.service_centric.global_dorm.models;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;

/**
 * Represents a room in the Global Dorm application.
 */
@lombok.Data
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private String location;

    @ElementCollection
    @CollectionTable(name = "room_languages", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "language")
    private Set<String> languagesSpoken;

    /**
     * Default constructor for JPA.
     */
    public Room() {
    }

    /**
     * Constructs a Room with the specified details.
     *
     * @param id              the room's ID
     * @param type            the type of the room
     * @param price           the price of the room
     * @param isAvailable     the availability status of the room
     * @param location        the location of the room
     * @param languagesSpoken the languages spoken in the room
     */
    public Room(Long id, String type, double price, boolean isAvailable, String location, Set<String> languagesSpoken) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.isAvailable = isAvailable;
        this.location = location;
        this.languagesSpoken = languagesSpoken;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public void setLanguagesSpoken(Set<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }
}
