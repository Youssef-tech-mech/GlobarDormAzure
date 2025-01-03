package ntu.service_centric.global_dorm.models;

import java.time.LocalDateTime;

public class Application {

    private String id;
    private String userId;
    private String roomId;
    private LocalDateTime applicationDate;
    private String status;

    public Application() {
    }


    public Application(String id, String userId, String roomId, LocalDateTime applicationDate, String status) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.applicationDate = applicationDate;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
