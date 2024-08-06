package dev.harshit.userservice.exceptions;

public class InvalidPasswordException extends Exception {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
