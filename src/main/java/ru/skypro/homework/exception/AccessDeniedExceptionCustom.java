package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedExceptionCustom extends RuntimeException {

    public AccessDeniedExceptionCustom(String message) {
        super(message);
    }

}
