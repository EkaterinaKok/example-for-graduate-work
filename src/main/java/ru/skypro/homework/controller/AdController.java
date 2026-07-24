package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdEntityService;

import java.io.IOException;

/**
 * Контроллер для управления объявлениями.
 * Предоставляет REST-эндпоинты для создания, чтения, обновления и удаления объявлений,
 * а также для работы с изображениями объявлений.
 */
@Tag(name = "Объявления")
@RequiredArgsConstructor
@RequestMapping("/ads")
@RestController
public class AdController {

    /**
     * Сервис для бизнес-логики работы с объявлениями.
     */
    private final AdEntityService adEntityService;

    /**
     * Получает список всех объявлений.
     *
     * @return объект {@link Ads}, содержащий коллекцию всех доступных объявлений
     */
    @Operation(summary = "Получение всех объявлений", operationId = "getAllAds")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Ads.class)))
    @GetMapping
    public Ads getAllAds() {
        return adEntityService.getAllAds();
    }

    /**
     * Добавляет новое объявление.
     * Поддерживает загрузку изображения через multipart-запрос.
     *
     * @param properties данные для создания/обновления объявления ({@link CreateOrUpdateAd})
     * @param image файл изображения объявления
     * @param authentication объект аутентификации текущего пользователя
     * @return созданное объявление в формате {@link Ad}
     * @throws IOException при ошибке обработки файла изображения
     */
    @Operation(summary = "Добавление объявления", operationId = "addAd")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = Ad.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAd(@Valid @RequestPart CreateOrUpdateAd properties, @RequestPart MultipartFile image, Authentication authentication) throws IOException {
        return adEntityService.addAd(properties, image, authentication);
    }

    /**
     * Получает подробную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления
     * @param authentication объект аутентификации текущего пользователя
     * @return расширенные данные об объявлении в формате {@link ExtendedAd}
     */
    @Operation(summary = "Получение информации об объявлении", operationId = "getAds")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ExtendedAd.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("{id}")
    public ExtendedAd getAds(@PathVariable int id, Authentication authentication) {
        return adEntityService.getAds(id, authentication);
    }

    /**
     * Удаляет объявление по его идентификатору.
     * Доступ разрешен только владельцу объявления или администратору.
     *
     * @param id идентификатор удаляемого объявления
     * @param authentication объект аутентификации текущего пользователя
     * @throws IOException при ошибке в процессе удаления
     */
    @Operation(summary = "Удаление объявления", operationId = "removeAd")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('USER') and @adEntityServiceImpl.isOwner(authentication.name, #id) or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void removeAd(@PathVariable int id, Authentication authentication) throws IOException {
        adEntityService.removeAd(id);
    }

    /**
     * Обновляет информацию об объявлении.
     * Доступ разрешен только владельцу объявления или администратору.
     *
     * @param id идентификатор обновляемого объявления
     * @param updateAd данные для обновления объявления ({@link CreateOrUpdateAd})
     * @param authentication объект аутентификации текущего пользователя
     * @return обновленное объявление в формате {@link Ad}
     */
    @Operation(summary = "Обновление информации об объявлении", operationId = "updateAds")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Ad.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('USER') and @adEntityServiceImpl.isOwner(authentication.name, #id) or hasRole('ADMIN')")
    @PatchMapping("{id}")
    public Ad updateAds(@PathVariable int id, @Valid @RequestBody CreateOrUpdateAd updateAd, Authentication authentication) {
        return adEntityService.updateAds(id, updateAd);
    }

    /**
     * Получает все объявления, принадлежащие авторизованному пользователю.
     *
     * @param authentication объект аутентификации текущего пользователя
     * @return коллекция объявлений пользователя в формате {@link Ads}
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя", operationId = "getAdsMe")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Ads.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/me")
    public Ads getAdsMe(Authentication authentication) {
        return adEntityService.getAdsMe(authentication);
    }

    /**
     * Обновляет изображение объявления.
     * Доступ разрешен только владельцу объявления или администратору.
     *
     * @param id идентификатор объявления, для которого обновляется изображение
     * @param image новый файл изображения
     * @param authentication объект аутентификации текущего пользователя
     * @return массив байтов обновленного изображения
     * @throws IOException при ошибке обработки файла изображения
     */
    @Operation(summary = "Обновление картинки объявления", operationId = "updateImage")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "byte")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PreAuthorize("hasRole('USER') and @adEntityServiceImpl.isOwner(authentication.name, #id) or hasRole('ADMIN')")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public byte[] updateImage(@PathVariable int id, @RequestParam MultipartFile image, Authentication authentication) throws IOException {
        return adEntityService.updateImage(id, image, authentication);
    }
}
