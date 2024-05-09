package ru.petrsushilin.testapp.requestservice.global.handler;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class GlobalAppErrorEntity {
    private int statusCode;
    private String message;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GlobalAppErrorEntity(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}

