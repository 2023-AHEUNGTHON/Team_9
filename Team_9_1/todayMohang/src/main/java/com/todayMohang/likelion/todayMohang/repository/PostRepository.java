package com.todayMohang.likelion.todayMohang.repository;

import com.todayMohang.likelion.todayMohang.domain.Post;
import com.todayMohang.likelion.todayMohang.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);

    @Query("SELECT p FROM Post p WHERE p.start <= :date AND p.end >= :date")
    List<Post> findAllByDate(@Param("date") LocalDateTime date);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.count = p.count + 1 WHERE p.id = :postId")
    void incrementCount(Long postId);
}