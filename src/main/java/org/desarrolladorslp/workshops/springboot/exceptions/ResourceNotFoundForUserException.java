package org.desarrolladorslp.workshops.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceNotFoundForUserException extends RuntimeException {
    public ResourceNotFoundForUserException(String msg) {
        super(msg);
    }
}
