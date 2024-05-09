package ru.petrsushilin.testapp.requestservice.global.exceptions;

/**
 * Exception that indicates that a method cannot change the status of a request.
 *
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class ChangeStatusException extends IllegalArgumentException {
    public ChangeStatusException(String message) {
        super(message);
    }
}
