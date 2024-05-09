package ru.petrsushilin.testapp.requestservice.requests.dto;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class RequestSetStageDTO {
    private Long requestID;
    private Long userID;
    private String currentStage;

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }
}
