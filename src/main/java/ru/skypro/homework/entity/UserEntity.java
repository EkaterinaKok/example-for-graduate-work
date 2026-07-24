package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Entity-класс, представляющий сущность пользователя в базе данных.
 * Отображается на таблицу "user_entities".
 * Содержит персональные данные пользователя, учетные данные (логин, пароль), роль (authority)
 * и ссылку на аватар. Также включает связь со списком объявлений пользователя.
 */
@ToString
@EqualsAndHashCode(of = "id")
@Setter
@Getter
@Entity(name = "user_entities")
public class UserEntity {

    /**
     * Уникальный идентификатор пользователя в базе данных.
     * Генерируется автоматически при создании записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Логин пользователя (email).
     * Должен быть уникальным, максимальная длина — 32 символа.
     */
    @Column(name = "username", unique = true, length = 32)
    private String username;

    /**
     * Пароль пользователя в зашифрованном виде.
     * Не может быть пустым.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Имя пользователя.
     * Не может быть пустым, максимальная длина — 16 символов.
     */
    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;

    /**
     * Фамилия пользователя.
     * Не может быть пустым, максимальная длина — 16 символов.
     */
    @Column(name = "last_name", nullable = false, length = 16)
    private String lastName;

    /**
     * Телефон пользователя.
     * Не может быть пустым, максимальная длина — 16 символов.
     */
    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    /**
     * Роль (полномочия) пользователя в системе (например, "USER" или "ADMIN").
     * Не может быть пустым, максимальная длина — 10 символов.
     */
    @Column(name = "authority", nullable = false, length = 10)
    private String authority;

    /**
     * Ссылка на изображение (аватар) пользователя.
     * Может отсутствовать.
     */
    @Column(name = "image")
    private String image;

    /**
     * Список объявлений, созданных данным пользователем (связь с сущностью AdEntity).
     * Поле исключено из строкового представления для предотвращения циклических ссылок.
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private List<AdEntity> adEntities;
}
