package ru.mihail.labs.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String message) {
        super(message);
    }
}
