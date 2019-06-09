package org.desarrolladorslp.workshops.springboot.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.desarrolladorslp.workshops.springboot.exceptions.EmailAlreadyRegisteredException;
import org.desarrolladorslp.workshops.springboot.exceptions.InternalServerException;
import org.desarrolladorslp.workshops.springboot.exceptions.ResourceNotFoundForUserException;
import org.desarrolladorslp.workshops.springboot.exceptions.UsernameAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    public static final String MESSAGE_FIELD = "message";
    public static final String TIMESTAMP_FIELD = "timestamp";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:SS");

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundException(EntityNotFoundException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> emailAlreadyRegisteredException(
            EmailAlreadyRegisteredException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, String>> usernameAlreadyRegisteredException(
            UsernameAlreadyRegisteredException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Map<String, String>> internalServerException(
            InternalServerException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundForUserException.class)
    public ResponseEntity<Map<String, String>> operationNotAllowedForUserException(
            ResourceNotFoundForUserException ex) {
        Map<String, String> response = new HashMap<>();

        response.put(MESSAGE_FIELD, ex.getMessage());
        response.put(TIMESTAMP_FIELD, LocalDateTime.now().format(formatter));

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
