package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Entity-класс, представляющий сущность комментария в базе данных.
 * Отображается на таблицу "comment_entities".
 * Содержит текст комментария, дату создания, а также связи с автором и объявлением.
 */
@ToString
@EqualsAndHashCode(of = "pk")
@Setter
@Getter
@Entity(name = "comment_entities")
public class CommentEntity {

    /**
     * Первичный ключ комментария в базе данных.
     * Генерируется автоматически при сохранении записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private int pk;

    /**
     * Текст комментария.
     * Не может быть пустым, максимальная длина — 64 символа.
     */
    @Column(name = "text", nullable = false, length = 64)
    private String text;

    /**
     * Дата и время создания комментария в миллисекундах.
     * Устанавливается автоматически перед сохранением записи.
     */
    @Column(name = "created_at", nullable = false)
    private long createdAt;

    /**
     * Автор комментария (связь с сущностью UserEntity).
     * Загрузка выполняется лениво (LAZY), связь обязательна.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserEntity author;

    /**
     * Объявление, к которому относится комментарий (связь с сущностью AdEntity).
     * Загрузка выполняется лениво (LAZY), связь обязательна, каскадное сохранение включено.
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ad_entity", nullable = false)
    private AdEntity adEntity;

    /**
     * Метод-слушатель JPA, вызываемый перед сохранением сущности.
     * Если поле createdAt не было установлено вручную, оно заполняется текущим временем.
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == 0) {
            createdAt = new Date().getTime();
        }
    }
}
