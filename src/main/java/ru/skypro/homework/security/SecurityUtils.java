package ru.skypro.homework.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skypro.homework.dto.Role;

public class SecurityUtils {

    // Берем значение из Enum. Если переименуешь enum, код подстроится сам.
    private static final String ADMIN_ROLE_NAME = Role.ADMIN.name();

    public static boolean hasAccess(Integer authorId, Integer currentUserId) {
        // 1. Проверка владельца (быстрая, не требует доступа к SecurityContext)
        if (authorId != null && authorId.equals(currentUserId)) {
            return true;
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();

        // Если пользователь не авторизован или у него нет прав - доступа нет
        if (auth == null || auth.getAuthorities() == null) {
            return false;
        }

        // 2. Строгая проверка роли администратора
        return auth.getAuthorities().stream()
                .anyMatch(SecurityUtils::isAdmin);
    }

    private static boolean isAdmin(GrantedAuthority authority) {
        String authorityName = authority.getAuthority();

        // ВАЖНО: Используем .equals() вместо .contains()!
        // Это закрывает замечание наставника про надежность проверки.

        // Вариант А: У тебя в БД хранится просто "ADMIN" (результат Role.ADMIN.name())
        if (authorityName.equals(ADMIN_ROLE_NAME)) {
            return true;
        }

        // Вариант Б: Страховка на случай, если Spring Security добавит префикс "ROLE_"
        // (например, если позже начнешь использовать @PreAuthorize)
        if (authorityName.equals("ROLE_" + ADMIN_ROLE_NAME)) {
            return true;
        }

        return false;
    }
}
