package ru.skypro.homework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.skypro.homework.dto.ErrorResponseDTO;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности Spring Security для приложения.
 * Настраивает цепочку фильтров безопасности (SecurityFilterChain),
 * управление пользователями через JDBC, кодирование паролей, CORS и обработку
 * ошибок аутентификации/авторизации.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    /**
     * Объект для сериализации и десериализации JSON-объектов.
     * Используется для формирования структурированных ответов об ошибках
     * при обработке исключений безопасности.
     */
    private final ObjectMapper objectMapper;

    /**
     * Массив URL-путей, для которых отключена обязательная аутентификация.
     * Включает эндпоинты Swagger UI, а также публичные пути для входа и регистрации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/login",
            "/register"
    };

    /**
     * Создает и настраивает {@link JdbcUserDetailsManager} для управления данными пользователей.
     * Переопределяет стандартные SQL-запросы для получения данных пользователя и его полномочий,
     * адаптируя их под схему таблицы {@code user_entities}.
     *
     * @param dataSource источник данных (DataSource), используемый для подключения к БД
     * @return настроенный экземпляр {@link JdbcUserDetailsManager}
     */
    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        manager.setUsersByUsernameQuery(
                "SELECT username, password, 1 as enabled FROM user_entities WHERE username = ?"
        );
        manager.setAuthoritiesByUsernameQuery(
                "SELECT username, authority FROM user_entities WHERE username = ?"
        );
        return manager;
    }

    /**
     * Определяет основную цепочку фильтров безопасности ({@link SecurityFilterChain}).
     * Настраивает:
     * <ul>
     *     <li>Отключение CSRF-защиты.</li>
     *     <li>Настройку CORS через {@link #corsConfigurationSource()}.</li>
     *     <li>Правила авторизации: публичный доступ к объявлениям, изображениям и белым спискам,
     *         остальные запросы требуют аутентификации.</li>
     *     <li>Базовую HTTP-аутентификацию.</li>
     *     <li>Кастомную обработку ошибок аутентификации и отказа в доступе.</li>
     * </ul>
     *
     * @param http объект конфигурации {@link HttpSecurity}
     * @return построенная цепочка фильтров {@link SecurityFilterChain}
     * @throws Exception в случае ошибки конфигурации цепочки фильтров
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/ads").permitAll()
                                .requestMatchers(HttpMethod.GET, "/ads/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::authenticationEntryPoint)
                        .accessDeniedHandler(this::accessDeniedHandler));
        return http.build();
    }

    /**
     * Создает и конфигурирует источник настроек CORS.
     * Разрешает запросы с адреса {@code http://localhost:3000}, поддерживает методы GET, POST, PUT, DELETE, OPTIONS, PATCH,
     * разрешает все заголовки и передачу учетных данных (cookies/authorization headers).
     *
     * @return экземпляр {@link UrlBasedCorsConfigurationSource} с настроенными правилами CORS
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Предоставляет {@link PasswordEncoder} для кодирования и проверки паролей пользователей.
     * В качестве реализации используется алгоритм BCrypt, обеспечивающий безопасное хеширование паролей.
     *
     * @return экземпляр {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Обработчик точки входа для ошибок аутентификации.
     * Формирует HTTP-ответ со статусом 401 (Unauthorized) и JSON-телом, содержащим детали ошибки.
     *
     * @param request HTTP-запрос, вызвавший ошибку
     * @param response HTTP-ответ, в который записывается результат
     * @param authException исключение, возникшее в процессе аутентификации
     * @throws IOException если произошла ошибка записи в выходной поток ответа
     */
    private void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Требуется аутентификация.",
                request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * Обработчик ошибок отказа в доступе (Authorization denied).
     * Формирует HTTP-ответ со статусом 403 (Forbidden) и JSON-телом, содержащим детали ошибки.
     *
     * @param request HTTP-запрос, вызвавший ошибку
     * @param response HTTP-ответ, в который записывается результат
     * @param accessDeniedException исключение, указывающее на отсутствие необходимых прав
     * @throws IOException если произошла ошибка записи в выходной поток ответа
     */
    private void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "У вас недостаточно прав для выполнения этого действия.",
                request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
