package ru.petrsushilin.testapp.requestservice.requests.dto;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class RequestCreationDTO {
    private Long userID;
    private String description;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
