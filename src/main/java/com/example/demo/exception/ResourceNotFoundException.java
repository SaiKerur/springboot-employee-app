package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException{

    //Interview question
    //Why do we always extend Runtime exception and why not compile time exception?
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
