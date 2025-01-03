package ntu.service_centric.global_dorm.models.api;

public class DistanceResponseDTO {
    private String roomId;
    private double distance;
    private String unit;

    public DistanceResponseDTO() {
    }

    public DistanceResponseDTO(String roomId, double distance, String unit) {
        this.roomId = roomId;
        this.distance = distance;
        this.unit = unit;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
