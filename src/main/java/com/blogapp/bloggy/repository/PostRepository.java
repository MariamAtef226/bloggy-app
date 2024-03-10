package com.blogapp.bloggy.repository;

import com.blogapp.bloggy.entity.Comment;
import com.blogapp.bloggy.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post>  findByCategoryId(Long categoryId);

    // JPQL queries
//    @Query("SELECT p from Post p WHERE " +
//            "p.title LIKE CONCAT('%',:query,'%') " +
//            "Or p.description LIKE CONCAT('%',:query,'%') " +
//            "Or p.description LIKE CONCAT('%',:query,'%') ")

    // SQL queries
    @Query(value = "SELECT * from posts p WHERE " +
            "p.title LIKE CONCAT('%',:query,'%') " +
            "or p.description LIKE CONCAT('%',:query,'%') " +
            "or p.content LIKE CONCAT('%',:query,'%')", nativeQuery = true)
    Page<Post> searchPosts(String query, Pageable pageable);
}
