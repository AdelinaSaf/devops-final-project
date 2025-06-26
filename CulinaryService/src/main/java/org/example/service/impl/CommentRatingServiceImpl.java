package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.converters.CommentConverter;
import org.example.dto.CommentCreateDTO;
import org.example.dto.CommentDTO;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.CommentRatingService;
import org.example.repository.*;
import org.example.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentRatingServiceImpl implements CommentRatingService {

    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentConverter commentConverter;

    @Override
    @Transactional
    public Comment addComment(Long recipeId, Long userId, String content) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    log.warn("Рецепт не найден: {}", recipeId);
                    return new EntityNotFoundException("Recipe not found");
                });
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", userId);
                    return new EntityNotFoundException("User not found");
                });

        CommentCreateDTO dto = new CommentCreateDTO();
        dto.setContent(content);
        Comment comment = commentConverter.toEntity(dto);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setRecipe(recipe);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Rating addRating(Long recipeId, Long userId, Double ratingValue) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    log.warn("Рецепт не найден: {}", recipeId);
                    return new EntityNotFoundException("Recipe not found");
                });
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", userId);
                    return new EntityNotFoundException("User not found");
                });

        Rating rating = ratingRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElse(new Rating());

        rating.setRating(ratingValue);
        rating.setRecipe(recipe);
        rating.setUser(user);

        return ratingRepository.save(rating);
    }

    @Override
    public List<Comment> getCommentsForRecipe(Long recipeId) {
        return commentRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId);
    }

    @Override
    public List<Comment> getCommentsForUser(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public double getUserAverageRating(Long userId) {
        return ratingRepository.calculateUserAverageRating(userId);
    }
    @Override
    public List<CommentDTO> getCommentsByRecipeId(Long recipeId) {
        return commentRepository.findByRecipeId(recipeId).stream()
                .map(commentConverter::toDTO)
                .collect(Collectors.toList());
    }
}
