package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UsersRepository;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UsersRepository usersRepository;

    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        String username = auth.getName();

        return usersRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow(() -> new IllegalStateException("Пользователь найден в контексте, но отсутствует в БД"));
    }
}
