package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarPath;
    private String createdAt;
    private boolean admin;
    private double userRating;
    private List<PreferenceDTO> preferences;

    private List<RecipePreviewDTO> createdRecipes;
    private List<RecipePreviewDTO> favoriteRecipes;
    private List<CommentDTO> comments;

    private int createdRecipesCount;
    private int favoriteRecipesCount;
    private int commentsCount;
}
