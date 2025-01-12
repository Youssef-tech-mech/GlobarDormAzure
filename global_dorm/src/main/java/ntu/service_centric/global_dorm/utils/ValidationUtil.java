package ntu.service_centric.global_dorm.utils;

import java.util.List;

public class ValidationUtil {

    public static void validateApplicationStatus(String status) {
        List<String> validStatuses = List.of("Pending", "Approved", "Rejected");
        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Allowed values are: " + validStatuses);
        }
    }

    public static void validateRoomAvailability(Boolean isAvailable) {
        if (isAvailable == null) {
            throw new IllegalArgumentException("Room availability must be specified.");
        }
    }
}
