package ru.petrsushilin.testapp.requestservice.requests.dto;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class RequestMessageDTO {
    private Long requestID;
    private Long authorID;
    private String description;

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
