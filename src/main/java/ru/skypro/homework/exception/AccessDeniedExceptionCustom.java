package ru.skypro.homework.exception;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedExceptionCustom extends AccessDeniedException {

    public AccessDeniedExceptionCustom(String message) {
        super(message);
    }

}
