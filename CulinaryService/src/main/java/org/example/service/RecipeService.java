package org.example.service;

import org.example.dto.*;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface RecipeService {
    RecipeResponseDTO createRecipe(RecipeCreateDTO recipeDTO, Long id);
    RecipeResponseDTO updateRecipe(Long recipeId, RecipeUpdateDTO recipeDTO);
    void deleteRecipe(Long recipeId, Long userId);
    RecipeResponseDTO findById(Long recipeId);
    Page<RecipeSummaryDTO> findAllRecipes(Pageable pageable);
    List<RecipeSummaryDTO> searchRecipes(String query, String category);
    List<RecipeSummaryDTO> findUserRecipes(Long userId);
    List<RecipeSummaryDTO> findFavoriteRecipes(Long userId);
    void addToFavorites(Long recipeId, User user);
    void removeFromFavorites(Long recipeId, Long userId);
    double calculateAverageRating(Long recipeId);
    List<RecipePreviewDTO> findByUserId(Long userId);
    List<RecipePreviewDTO> findAllByUserId(Long userId);
    boolean isFavorite(Long recipeId, Long userId);
    public List<RecipeApiResponse> searchRecipesApi(String query, String category);
    public Page<RecipeApiResponse> findAllRecipesApi(Pageable pageable);

}