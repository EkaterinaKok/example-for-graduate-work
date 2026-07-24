package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Entity-класс, представляющий сущность объявления в базе данных.
 * Отображается на таблицу "ad_entities".
 * Содержит основные атрибуты объявления (заголовок, описание, цена, изображение),
 * а также связи с автором (UserEntity) и списком комментариев (CommentEntity).
 */
@ToString
@EqualsAndHashCode(of = "pk")
@Setter
@Getter
@Entity(name = "ad_entities")
public class AdEntity {

    /**
     * Первичный ключ объявления в базе данных.
     * Генерируется автоматически при сохранении записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private int pk;

    /**
     * Заголовок объявления.
     * Не может быть пустым, максимальная длина — 32 символа.
     */
    @Column(name = "title", nullable = false, length = 32)
    private String title;

    /**
     * Описание объявления.
     * Не может быть пустым, максимальная длина — 64 символа.
     */
    @Column(name = "description", nullable = false, length = 64)
    private String description;

    /**
     * Цена объявления.
     * Не может быть пустой.
     */
    @Column(name = "price", nullable = false)
    private int price;

    /**
     * Ссылка на изображение объявления.
     * Не может быть пустой.
     */
    @Column(name = "image", nullable = false)
    private String image;

    /**
     * Автор объявления (связь с сущностью UserEntity).
     * Загрузка выполняется лениво (LAZY), связь обязательна.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserEntity author;

    /**
     * Список комментариев к объявлению (связь с сущностью CommentEntity).
     * Комментарии сортируются по дате создания в порядке убывания.
     * При удалении объявления все связанные комментарии также удаляются (cascade = REMOVE, orphanRemoval = true).
     */
    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "adEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> commentEntities;
}
