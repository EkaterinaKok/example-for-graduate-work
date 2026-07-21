package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(of = "pk")
@Setter
@Getter
@Entity(name = "ad_entities")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private int pk;

    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Column(name = "description", nullable = false, length = 64)
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "image", nullable = false)
    private String image;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private UserEntity author;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "adEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> commentEntities;

}
