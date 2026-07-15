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

    /**
     * Получает ID текущего авторизованного пользователя из SecurityContext.
     * Если пользователь не авторизован, выбрасывает исключение.
     */
    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не авторизован");
        }

        // Получаем username из контекста (это то, что мы положили в CustomUserDetailsService)
        String username = auth.getName();

        // Ищем пользователя в БД по username, чтобы получить его Integer ID
        return usersRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow(() -> new IllegalStateException("Пользователь найден в контексте, но отсутствует в БД"));
    }
}
