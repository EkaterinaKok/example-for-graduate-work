package ru.skypro.homework.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";

        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Хэш для пароля '" + rawPassword + "': " + encodedPassword);
    }

}
