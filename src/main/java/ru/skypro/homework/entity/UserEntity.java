package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode(of = "id")
@Setter
@Getter
@Entity(name = "user_entities")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", unique = true, length = 32)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 16)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 16)
    private String lastName;

    @Column(name = "phone", nullable = false, length = 16)
    private String phone;

    @Column(name = "authority", nullable = false, length = 10)
    private String authority;

    @Column(name = "image")
    private String image;

    @ToString.Exclude
    @OneToMany(mappedBy = "author")
    private List<AdEntity> adEntities;
}
