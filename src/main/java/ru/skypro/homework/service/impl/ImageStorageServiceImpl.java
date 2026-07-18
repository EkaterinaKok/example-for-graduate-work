package ru.skypro.homework.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import ru.skypro.homework.service.ImageStorageService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private final Path uploadDirectory;

    public ImageStorageServiceImpl() {
        String basePath = System.getProperty("user.dir");
        this.uploadDirectory = Paths.get(basePath, "uploads", "images");

        File directory = uploadDirectory.toFile();
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Папка создана: " + uploadDirectory.toAbsolutePath());
            } else {
                throw new RuntimeException("Не удалось создать папку для изображений!");
            }
        }
    }

    @Override
    public String saveImage(byte[] bytes, String filename) {
        Path filePath = uploadDirectory.resolve(filename).normalize();
        if (!filePath.toAbsolutePath().startsWith(uploadDirectory.toAbsolutePath())) {
            throw new SecurityException("Недопустимое имя файла");
        }

        try {
            Files.write(filePath, bytes);
            return "uploads/images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    @Override
    public Resource getResource(String filename) throws MalformedURLException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Имя файла не может быть пустым");
        }

        Path filePath = uploadDirectory.resolve(filename).normalize();

        if (!filePath.toAbsolutePath().startsWith(uploadDirectory.toAbsolutePath())) {
            return null;
        }

        if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
            return new UrlResource(filePath.toUri());
        } else {
            return null;
        }
    }

}
