package ru.skypro.homework.exception;

public class UserEntityNotFoundException extends RuntimeException {

    public UserEntityNotFoundException(String message) {
        super(message);
    }
}
