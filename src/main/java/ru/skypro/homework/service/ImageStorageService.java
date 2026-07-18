package ru.skypro.homework.service;

import org.springframework.core.io.Resource;

import java.net.MalformedURLException;

public interface ImageStorageService {

    String saveImage(byte[] bytes, String filename);
    Resource getResource(String filename) throws MalformedURLException;

}
