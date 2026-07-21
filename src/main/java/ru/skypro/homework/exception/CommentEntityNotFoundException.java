package ru.skypro.homework.exception;

public class CommentEntityNotFoundException extends RuntimeException {

    public CommentEntityNotFoundException(String message) {
        super(message);
    }
}
