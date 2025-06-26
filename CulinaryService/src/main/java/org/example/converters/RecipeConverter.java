package org.example.converters;

import org.example.dto.*;
import org.example.entity.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeConverter {

    public Recipe toEntity(RecipeCreateDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setCategory(dto.getCategory());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setServings(dto.getServings());
        recipe.setIngredients(dto.getIngredients());
        recipe.setSteps(dto.getSteps());
        return recipe;
    }

    public void updateEntity(RecipeUpdateDTO dto, Recipe entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getCategory() != null) entity.setCategory(dto.getCategory());
        if (dto.getPreparationTime() != null) entity.setPreparationTime(dto.getPreparationTime());
        if (dto.getServings() != null) entity.setServings(dto.getServings());
        if (dto.getIngredients() != null) entity.setIngredients(dto.getIngredients());
        if (dto.getSteps() != null) entity.setSteps(dto.getSteps());
    }

    public RecipeResponseDTO toResponseDTO(Recipe recipe,
                                           List<String> additionalImages,
                                           double averageRating) {
        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setCategory(recipe.getCategory());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setServings(recipe.getServings());
        dto.setIngredients(recipe.getIngredients());
        dto.setSteps(recipe.getSteps());
        dto.setCreatedAt(recipe.getCreatedAt().toString());
        dto.setCoverImagePath(recipe.getCoverImagePath());
        dto.setAdditionalImages(additionalImages);
        dto.setUserId(recipe.getUser().getId());
        dto.setAuthorName(recipe.getUser().getUsername());
        dto.setAverageRating(averageRating);
        return dto;
    }

    public RecipeSummaryDTO toSummaryDTO(Recipe recipe, double averageRating) {
        RecipeSummaryDTO dto = new RecipeSummaryDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setCategory(recipe.getCategory());
        dto.setDescription(recipe.getDescription());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setCoverImagePath(recipe.getCoverImagePath());
        dto.setAuthorName(recipe.getUser().getUsername());
        dto.setAverageRating(averageRating);
        return dto;
    }
    public RecipePreviewDTO toPreviewDTO(Recipe recipe) {
        RecipePreviewDTO dto = new RecipePreviewDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setCategory(recipe.getCategory());
        dto.setCoverImagePath(recipe.getCoverImagePath());
        dto.setDescription(recipe.getDescription());
        dto.setPreparationTime(recipe.getPreparationTime());
        dto.setServings(recipe.getServings());
        dto.setCreatedAt(recipe.getCreatedAt());
        return dto;
    }
}
