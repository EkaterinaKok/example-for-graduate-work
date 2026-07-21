package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveImage(MultipartFile multipartFile) throws IOException;

    byte[] getImage(String filePath) throws IOException;

    void deleteImage(String filePath) throws IOException;
}