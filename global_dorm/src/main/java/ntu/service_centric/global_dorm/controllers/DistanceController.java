package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.services.api.DistanceApiService;
import ntu.service_centric.global_dorm.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/distance")
public class DistanceController {

    @Autowired
    private DistanceApiService distanceApiService;

    @Autowired
    private RoomService roomService;

    // Render distance input form with room options
    @GetMapping
    public String showDistanceForm(Model model) {
        List<Room> availableRooms = roomService.getAllRooms(); // Fetch available rooms
        model.addAttribute("rooms", availableRooms);
        return "distance"; // Corresponds to distance.html
    }

    // Calculate and display the distance results
    @GetMapping("/calculate")
    public String calculateDistance(
            @RequestParam String profile, // driving, cycling, walking
            @RequestParam(required = false) String startCity, // Starting city
            @RequestParam(required = false) Integer roomID, // Selected room as starting point
            @RequestParam String endCity, // Destination city
            Model model
    ) {
        String startCoordinates;
        String endCoordinates = distanceApiService.resolveCityToCoordinates(endCity);

        if (roomID != null) {
            // Use room as the starting point
            Room room = roomService.getRoomById(roomID)
                    .orElseThrow(() -> new RuntimeException("Room not found with ID " + roomID));
            if (room.getLocation() == null) {
                throw new IllegalArgumentException("Room with ID " + roomID + " does not have a valid location.");
            }
            startCoordinates = room.getLocation().getLongitude() + "," + room.getLocation().getLatitude();
        } else if (startCity != null) {
            // Use city name as the starting point
            startCoordinates = distanceApiService.resolveCityToCoordinates(startCity);
        } else {
            throw new IllegalArgumentException("Either startCity or roomID must be provided.");
        }

        Map<String, Object> response = distanceApiService.calculateDistance(profile, startCoordinates, endCoordinates);

        // Process routes for display
        List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
        for (Map<String, Object> route : routes) {
            // Handle distance and duration fields dynamically
            double distanceInKm = convertToDouble(route.get("distance")) / 1000.0; // Convert meters to kilometers
            route.put("distance", String.format("%.2f km", distanceInKm)); // Format distance as string

            double durationInSeconds = convertToDouble(route.get("duration"));
            int hours = (int) (durationInSeconds / 3600);
            int minutes = (int) ((durationInSeconds % 3600) / 60);
            route.put("duration", String.format("%d hours %d minutes", hours, minutes)); // Format time
        }

        // Pass data to the model
        model.addAttribute("routes", routes);
        model.addAttribute("waypoints", response.get("waypoints"));
        return "distance-result";
    }

    // Helper method to convert Object to Double
    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unexpected value type: " + value.getClass().getName());
        }
    }
}