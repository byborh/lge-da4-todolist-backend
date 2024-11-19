package com.todolist.backend.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, Object identifier) {
        super("The resource of type "+resourceName+" with identifier "+identifier+" already exists.");
    }
}
