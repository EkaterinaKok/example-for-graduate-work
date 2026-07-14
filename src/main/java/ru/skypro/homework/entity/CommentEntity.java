package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @Column(nullable = false, length = 64)
    private String text;

    @Column(name = "created_at", nullable = false)
    private Long createdAt; // Храним timestamp в мс (Long)

    // Связь: комментарий принадлежит одному пользователю. Lazy обязательно!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    // Связь: комментарий относится к одному объявлению. Lazy обязательно!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", nullable = false)
    private AdEntity ad;
}
