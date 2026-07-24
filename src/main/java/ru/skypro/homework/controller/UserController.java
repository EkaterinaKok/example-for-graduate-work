package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserEntityService;

import java.io.IOException;

/**
 * Контроллер для управления данными пользователя.
 * Предоставляет REST-эндпоинты для обновления пароля, получения и обновления профиля,
 * а также загрузки аватара авторизованного пользователя.
 */
@Tag(name = "Пользователи")
@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    /**
     * Сервис, реализующий бизнес-логику работы с пользователями.
     */
    private final UserEntityService userEntityService;

    /**
     * Обновляет пароль авторизованного пользователя.
     * Доступен только аутентифицированным пользователям.
     *
     * @param newPassword данные с новым паролем ({@link NewPassword})
     * @param authentication объект аутентификации текущего пользователя
     */
    @Operation(summary = "Обновление пароля", operationId = "setPassword")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/set_password")
    public void setPassword(@Valid @RequestBody NewPassword newPassword, Authentication authentication) {
        userEntityService.setPassword(newPassword, authentication);
    }

    /**
     * Получает информацию о текущем авторизованном пользователе.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @return данные пользователя в формате {@link User}
     */
    @Operation(summary = "Получение информации об авторизованном пользователе", operationId = "getUser")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/me")
    public User getUser(Authentication authentication) {
        return userEntityService.getUser(authentication);
    }

    /**
     * Обновляет персональные данные авторизованного пользователя.
     *
     * @param updateUser данные для обновления профиля пользователя ({@link UpdateUser})
     * @param authentication объект аутентификации текущего пользователя
     * @return обновленные данные пользователя в формате {@link UpdateUser}
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе", operationId = "updateUser")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UpdateUser.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PatchMapping("/me")
    public UpdateUser updateUser(@Valid @RequestBody UpdateUser updateUser, Authentication authentication) {
        return userEntityService.updateUser(updateUser, authentication);
    }

    /**
     * Обновляет аватар (изображение профиля) авторизованного пользователя.
     * Принимает файл в формате multipart/form-data.
     *
     * @param image загружаемый файл изображения ({@link MultipartFile})
     * @param authentication объект аутентификации текущего пользователя
     * @throws IOException при ошибке обработки файла
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя", operationId = "updateUserImage")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUserImage(@RequestParam MultipartFile image, Authentication authentication) throws IOException {
        userEntityService.updateUserImage(image, authentication);
    }
}
