package org.example.repository;

import org.example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeIdOrderByCreatedAtDesc(Long recipeId);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByRecipeId(Long recipeId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
    void deleteByRecipeId(Long recipeId);
}
