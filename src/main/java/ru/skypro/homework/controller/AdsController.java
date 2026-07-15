package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.security.AuthUtils;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService adsService; // <-- Внедряем интерфейс
    private final AuthUtils authUtils;

    @GetMapping
    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Ads> getAllAds() {
        log.info("Запрос на получение всех объявлений");
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Добавление объявления")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Ad> addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) {

        Integer currentUserId = authUtils.getCurrentUserId();
        Ad ad = adsService.addAd(properties, currentUserId, image);
        return ResponseEntity.status(201).body(ad);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        log.info("Запрос объявления с ID: {}", id);

        // ✅ ТЕПЕРЬ ВЫЗЫВАЕМ МЕТОД ЧЕРЕЗ ИНТЕРФЕЙС. Приведение типов УДАЛЕНО.
        ExtendedAd ad = adsService.getExtendedAdById(id);

        return ResponseEntity.ok(ad);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        Integer currentUserId = authUtils.getCurrentUserId();
        adsService.removeAd(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id, @RequestBody CreateOrUpdateAd dto) {
        Integer currentUserId = authUtils.getCurrentUserId();
        Ad ad = adsService.updateAd(id, dto, currentUserId);
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизированного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Ads> getAdsMe() {
        Integer currentUserId = authUtils.getCurrentUserId();
        Ads ads = adsService.getAdsByAuthor(currentUserId);
        return ResponseEntity.ok(ads);
    }

    @PatchMapping("/{id}/image")
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<?> updateImage(@PathVariable Integer id, @RequestPart("image") MultipartFile image) {
        Integer currentUserId = authUtils.getCurrentUserId();
        Ad updatedAd = adsService.updateImage(id, image, currentUserId);
        return ResponseEntity.ok(updatedAd);
    }
}
