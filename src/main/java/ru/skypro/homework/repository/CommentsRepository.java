package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

public interface CommentsRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("SELECT c FROM CommentEntity c WHERE c.ad.pk = :adPk")
    List<CommentEntity> findAllByAdPk(@Param("adPk") Integer adPk);

}
