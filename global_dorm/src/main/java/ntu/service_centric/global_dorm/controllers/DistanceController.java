package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.Room;
import ntu.service_centric.global_dorm.services.api.DistanceApiService;
import ntu.service_centric.global_dorm.services.api.PostcodeGeoService;
import ntu.service_centric.global_dorm.services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DistanceController.class);

    @Autowired
    private DistanceApiService distanceApiService;

    @Autowired
    private PostcodeGeoService postcodeGeoService;

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String showDistanceForm(Model model) {
        List<Room> availableRooms = roomService.getAllRooms();
        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "No available rooms to display.");
        } else {
            model.addAttribute("rooms", availableRooms);
        }
        return "distance";
    }

    @GetMapping("/calculate")
    public String calculateDistance(
            @RequestParam String profile,
            @RequestParam(required = false) String startCity,
            @RequestParam(required = false) String startPostcode,
            @RequestParam(required = false) Integer roomID,
            @RequestParam(required = false) String endCity,
            @RequestParam(required = false) String endPostcode,
            Model model
    ) {
        try {
            // Resolve start and end coordinates
            String startCoordinates = resolveStartCoordinates(startCity, startPostcode, roomID);
            String endCoordinates = resolveEndCoordinates(endCity, endPostcode);

            logger.info("Calculating distance between {} and {} with profile {}", startCoordinates, endCoordinates, profile);

            // Call distance API (caching handled by DistanceApiService)
            Map<String, Object> response = distanceApiService.calculateDistance(profile, startCoordinates, endCoordinates);

            // Process and format the API response
            List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
            processRoutesForDisplay(routes);

            // Populate model with response data
            model.addAttribute("routes", routes);
            model.addAttribute("waypoints", response.get("waypoints"));
            model.addAttribute("startPostcode", startPostcode != null ? startPostcode.trim().toUpperCase() : startCity);
            model.addAttribute("endPostcode", endPostcode != null ? endPostcode.trim().toUpperCase() : endCity);

            logger.info("Successfully calculated distance. Returning results to the view.");

            return "distance-result";

        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "distance";
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "distance";
        }
    }
    /**
     * Resolve start coordinates based on input parameters.
     */
    private String resolveStartCoordinates(String startCity, String startPostcode, Integer roomID) {
        if (roomID != null) {
            // Fetch room and use its postcode
            Room room = roomService.getRoomById(roomID)
                    .orElseThrow(() -> new IllegalArgumentException("Room not found with ID " + roomID));
            if (room.getPostcode() == null) {
                throw new IllegalArgumentException("Room with ID " + roomID + " does not have a valid postcode.");
            }
            return postcodeGeoService.getCoordinates(room.getPostcode());
        } else if (startPostcode != null && !startPostcode.trim().isEmpty()) {
            // Use provided postcode
            validatePostcode(startPostcode);
            return postcodeGeoService.getCoordinates(startPostcode.trim().toUpperCase());
        } else if (startCity != null && !startCity.trim().isEmpty()) {
            // Resolve city to coordinates
            return distanceApiService.resolveCityToCoordinates(startCity.trim());
        } else {
            throw new IllegalArgumentException("Please provide either a starting city, postcode, or select a room.");
        }
    }

    /**
     * Resolve end coordinates based on input parameters.
     */
    private String resolveEndCoordinates(String endCity, String endPostcode) {
        if (endPostcode != null && !endPostcode.trim().isEmpty()) {
            // Use provided postcode
            validatePostcode(endPostcode);
            return postcodeGeoService.getCoordinates(endPostcode.trim().toUpperCase());
        } else if (endCity != null && !endCity.trim().isEmpty()) {
            // Resolve city to coordinates
            return distanceApiService.resolveCityToCoordinates(endCity.trim());
        } else {
            throw new IllegalArgumentException("Please provide either an ending city or postcode.");
        }
    }

    /**
     * Validate UK postcode format.
     */
    private void validatePostcode(String postcode) {
        if (!isValidPostcode(postcode)) {
            throw new IllegalArgumentException("Invalid postcode format: " + postcode);
        }
    }

    /**
     * Process routes to format distance and duration for display.
     */
    private void processRoutesForDisplay(List<Map<String, Object>> routes) {
        for (Map<String, Object> route : routes) {
            // Format distance
            double distanceInKm = safelyConvertToDouble(route.get("distance")) / 1000.0;
            route.put("formattedDistance", String.format("%.2f km", distanceInKm));

            // Format duration
            double durationInSeconds = safelyConvertToDouble(route.get("duration"));
            int hours = (int) (durationInSeconds / 3600);
            int minutes = (int) ((durationInSeconds % 3600) / 60);
            route.put("formattedDuration", String.format("%d hours %d minutes", hours, minutes));
        }
    }

    /**
     * Safely convert an object to a double value.
     */
    private double safelyConvertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unable to parse String to Double: " + value, e);
            }
        } else {
            throw new IllegalArgumentException("Unexpected value type: " + (value != null ? value.getClass().getName() : "null"));
        }
    }

    /**
     * Check if the postcode format is valid.
     */
    private boolean isValidPostcode(String postcode) {
        return postcode != null && postcode.matches("^[A-Z]{1,2}[0-9][A-Z0-9]?\\s?[0-9][A-Z]{2}$");
    }
}
