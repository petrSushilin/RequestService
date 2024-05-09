package ru.petrsushilin.testapp.requestservice.global.exceptions;

/**
 * Exception that indicates that the identifier of the object does not match the expected one.
 *
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
public class IdentifierMismatchException extends IllegalArgumentException {
    public IdentifierMismatchException(String message) {
        super(message);
    }
}
