package com.docker.setup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Sets the HTTP status code
public class ResourceNotFoundException extends RuntimeException {

    // Standard constructor
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor for cleaner usage
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
