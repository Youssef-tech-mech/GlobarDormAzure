package ntu.service_centric.global_dorm.controllers;

import ntu.service_centric.global_dorm.models.api.DistanceResponseDTO;
import ntu.service_centric.global_dorm.services.DistanceServiceClient;
import ntu.service_centric.global_dorm.services.api.DistanceApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/distance")
public class DistanceController {

    @Autowired
    private DistanceApiService distanceApiService;

    /**
     * Calculate distance between the selected room's location and a specific campus/location.
     *
     * @param roomId ID of the room selected by the user
     * @param campusLat Latitude of the campus location
     * @param campusLon Longitude of the campus location
     * @return DistanceResponseDTO with calculated distance
     */
    @GetMapping("/{roomId}")
    public DistanceResponseDTO calculateDistanceToCampus(
            @PathVariable("roomId") String roomId,
            @RequestParam("campusLat") double campusLat,
            @RequestParam("campusLon") double campusLon) {
        return distanceApiService.calculateDistanceToCampus(roomId, campusLat, campusLon);
    }
}