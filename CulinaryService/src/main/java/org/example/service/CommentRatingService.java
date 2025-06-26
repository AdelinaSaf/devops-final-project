package org.example.service;

import org.example.dto.CommentDTO;
import org.example.entity.Comment;
import org.example.entity.Rating;
import org.example.entity.Recipe;
import org.example.entity.User;

import java.util.List;

public interface CommentRatingService {
    Comment addComment(Long recipeId, Long userId, String content);
    void deleteComment(Long commentId);
    Rating addRating(Long recipeId, Long userId, Double rating);
    List<Comment> getCommentsForRecipe(Long recipeId);
    List<Comment> getCommentsForUser(Long userId);
    double getUserAverageRating(Long userId);
    List<CommentDTO> getCommentsByRecipeId(Long recipeId);
}
