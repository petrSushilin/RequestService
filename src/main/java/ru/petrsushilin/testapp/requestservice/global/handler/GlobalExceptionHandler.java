package ru.petrsushilin.testapp.requestservice.global.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.petrsushilin.testapp.requestservice.global.exceptions.ChangeStatusException;
import ru.petrsushilin.testapp.requestservice.global.exceptions.IdentifierMismatchException;

/**
 * @author Petr Sushilin
 * @version 1.0
 * @since 05.05.2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleNumberFormatException(NumberFormatException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IdentifierMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleIdentifierMismatchException(IdentifierMismatchException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChangeStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleChangeStatusException(ChangeStatusException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleMethodArgumentTypeMismatchException
            (MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<GlobalAppErrorEntity> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new GlobalAppErrorEntity(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
