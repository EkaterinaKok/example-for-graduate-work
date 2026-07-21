package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode(of = "pk")
@Setter
@Getter
@Entity(name = "comment_entities")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private int pk;

    @Column(name = "text", nullable = false, length = 64)
    private String text;

    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserEntity author;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ad_entity", nullable = false)
    private AdEntity adEntity;

    @PrePersist
    protected void onCreate() {
        if (createdAt == 0) {
            createdAt = new Date().getTime();
        }
    }
}
