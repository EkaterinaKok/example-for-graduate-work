package ru.skypro.homework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация веб-MVC для настройки обработки статических ресурсов.
 * Отвечает за регистрацию обработчиков ресурсов (в частности, изображений),
 * позволяя отдавать файлы из файловой системы по HTTP-запросам.
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Путь к директории с изображениями, задаваемый через свойство конфигурации.
     * Значение подставляется из application.properties/yml по ключу {@code path.dir.image}.
     */
    @Value("${path.dir.image}")
    private String imagePath;

    /**
     * Регистрирует обработчики ресурсов для обработки запросов к статическим файлам.
     * Настраивает маппинг URL-пути {@code /images/**} на физическое расположение файлов
     * в файловой системе. Путь формируется динамически в зависимости от наличия
     * системной переменной окружения SPRING_DATASOURCE_URL.
     *
     * @param registry реестр обработчиков ресурсов, используемый для регистрации новых маппингов
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + (System.getenv("SPRING_DATASOURCE_URL") == null ? System.getProperty("user.dir") : "") + imagePath);
    }
}