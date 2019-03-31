package org.desarrolladorslp.workshops.springboot.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

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
}
