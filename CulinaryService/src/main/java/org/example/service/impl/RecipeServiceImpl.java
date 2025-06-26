package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.converters.RecipeConverter;
import org.example.dto.*;
import org.example.entity.*;
import org.example.exceptions.AccessDeniedException;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.repository.*;
import org.example.service.FileService;
import org.example.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ImageRecipeRepository imageRecipeRepository;
    private final RatingRepository ratingRepository;
    private final UserFavoriteRecipeRepository userFavoriteRecipeRepository;
    private final FileService fileService;
    private final RecipeConverter recipeConverter;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public RecipeResponseDTO createRecipe(RecipeCreateDTO recipeDTO, Long userId) {
        try {
            User author = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            Recipe recipe = recipeConverter.toEntity(recipeDTO);
            recipe.setUser(author);
            recipe.setCreatedAt(LocalDateTime.now());

            if (recipeDTO.getCoverImage() != null && !recipeDTO.getCoverImage().isEmpty()) {
                recipe.setCoverImagePath(fileService.saveFile(recipeDTO.getCoverImage()));
            }

            Recipe savedRecipe = recipeRepository.save(recipe);
            if (recipeDTO.getImages() != null) {
                for (MultipartFile image : recipeDTO.getImages()) {
                    if (!image.isEmpty()) {
                        ImageRecipe img = new ImageRecipe();
                        img.setPath(fileService.saveFile(image));
                        img.setRecipe(savedRecipe);
                        imageRecipeRepository.save(img);
                    }
                }
            }

            List<String> additionalImages = imageRecipeRepository.findByRecipeId(savedRecipe.getId())
                    .stream()
                    .map(ImageRecipe::getPath)
                    .collect(Collectors.toList());

            double averageRating = calculateAverageRating(savedRecipe.getId());
            return recipeConverter.toResponseDTO(savedRecipe, additionalImages, averageRating);

        } catch (IOException e) {
            log.error("Ошибка при сохранении изображения для рецепта: {}", recipeDTO.getName(), e);
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    @Transactional
    public RecipeResponseDTO updateRecipe(Long recipeId, RecipeUpdateDTO recipeDTO) {
        try {
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> {
                        log.warn("Рецепт не найден: {}", recipeId);
                        return new EntityNotFoundException("Recipe not found");
                    });

            if (recipeDTO.getCoverImage() != null && !recipeDTO.getCoverImage().isEmpty()) {
                if (recipe.getCoverImagePath() != null) {
                    fileService.deleteFile(recipe.getCoverImagePath());
                }
                recipe.setCoverImagePath(fileService.saveFile(recipeDTO.getCoverImage()));
            }

            if (recipeDTO.getImages() != null && !recipeDTO.getImages().isEmpty()) {
                List<ImageRecipe> oldImages = imageRecipeRepository.findByRecipeId(recipeId);
                for (ImageRecipe img : oldImages) {
                    fileService.deleteFile(img.getPath());
                    imageRecipeRepository.delete(img);
                }

                for (MultipartFile image : recipeDTO.getImages()) {
                    if (!image.isEmpty()) {
                        ImageRecipe img = new ImageRecipe();
                        img.setPath(fileService.saveFile(image));
                        img.setRecipe(recipe);
                        imageRecipeRepository.save(img);
                    }
                }
            }
            recipeConverter.updateEntity(recipeDTO, recipe);


            Recipe updatedRecipe = recipeRepository.save(recipe);
            List<String> additionalImages = imageRecipeRepository.findByRecipeId(recipeId)
                    .stream()
                    .map(ImageRecipe::getPath)
                    .collect(Collectors.toList());

            double averageRating = calculateAverageRating(recipeId);
            return recipeConverter.toResponseDTO(updatedRecipe, additionalImages, averageRating);
        } catch (IOException e) {
            log.error("Ошибка при обновлении изображения для рецепта: {}", recipeId, e);
            throw new RuntimeException("Failed to update image", e);
        }
    }

    @Override
    @Transactional
    public void deleteRecipe(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the owner of this recipe");
        }
        if (recipe.getCoverImagePath() != null) {
            fileService.deleteFile(recipe.getCoverImagePath());
        }
        List<ImageRecipe> images = imageRecipeRepository.findByRecipeId(recipeId);
        for (ImageRecipe image : images) {
            fileService.deleteFile(image.getPath());
            imageRecipeRepository.delete(image);
        }
        userFavoriteRecipeRepository.deleteByRecipeId(recipeId);

        ratingRepository.deleteByRecipeId(recipeId);

        // Удаление комментариев
        commentRepository.deleteByRecipeId(recipeId);

        recipeRepository.delete(recipe);
    }

    @Override
    public RecipeResponseDTO findById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {
                    log.warn("Рецепт не найден: {}", recipeId);
                    return new EntityNotFoundException("Recipe not found");
                });
        List<String> additionalImages = imageRecipeRepository.findByRecipeId(recipeId)
                .stream()
                .map(ImageRecipe::getPath)
                .collect(Collectors.toList());

        double averageRating = calculateAverageRating(recipeId);
        return recipeConverter.toResponseDTO(recipe, additionalImages, averageRating);
    }

    @Override
    public Page<RecipeSummaryDTO> findAllRecipes(Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(pageable);
        return recipes.map(recipe -> {
            double avgRating = calculateAverageRating(recipe.getId());
            return recipeConverter.toSummaryDTO(recipe, avgRating);
        });
    }

    @Override
    public List<RecipeSummaryDTO> searchRecipes(String query, String category) {
        String normalizedQuery = (query != null && !query.trim().isEmpty()) ? query.trim().toLowerCase() : null;
        String normalizedCategory = (category != null && !category.isEmpty()) ? category : null;

        boolean hasCriteria = (normalizedQuery != null) || (normalizedCategory != null);

        List<Recipe> recipes;

        if (hasCriteria) {
            recipes = recipeRepository.searchRecipes(normalizedQuery, normalizedCategory);
        } else {
            // Если критериев нет - возвращаем все рецепты
            recipes = recipeRepository.findAll();
        }

        return recipes.stream()
                .map(recipe -> recipeConverter.toSummaryDTO(recipe, calculateAverageRating(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeSummaryDTO> findUserRecipes(Long userId) {
        List<Recipe> recipes = recipeRepository.findByUserId(userId);
        return recipes.stream()
                .map(recipe -> recipeConverter.toSummaryDTO(recipe, calculateAverageRating(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeSummaryDTO> findFavoriteRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user.getFavoriteRecipes().stream()
                .map(recipe -> recipeConverter.toSummaryDTO(recipe, calculateAverageRating(recipe.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addToFavorites(Long recipeId, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        // Используем репозиторий для проверки существования связи
        if (userFavoriteRecipeRepository.existsByUserIdAndRecipeId(user.getId(), recipeId)) {
            throw new AlreadyExistsException("Recipe is already in favorites");
        }

        UserFavoriteRecipe ufr = new UserFavoriteRecipe();
        ufr.setUser(user);
        ufr.setRecipe(recipe);
        userFavoriteRecipeRepository.save(ufr);
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long recipeId, Long userId) {
        userFavoriteRecipeRepository.deleteByUserIdAndRecipeId(userId, recipeId);
    }

    @Override
    public double calculateAverageRating(Long recipeId) {
        Double rating = ratingRepository.calculateAverageRating(recipeId);
        return rating != null ? rating : 0.0;
    }

    @Override
    public List<RecipePreviewDTO> findByUserId(Long userId) {
        return recipeRepository.findByUserId(userId).stream()
                .map(recipeConverter::toPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipePreviewDTO> findAllByUserId(Long userId) {
        List<Recipe> recipes = recipeRepository.findByUserId(userId);
        return recipes.stream()
                .map(recipeConverter::toPreviewDTO)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isFavorite(Long recipeId, Long userId) {
        return userFavoriteRecipeRepository.existsByUserIdAndRecipeId(userId, recipeId);
    }
    @Override
    public List<RecipeApiResponse> searchRecipesApi(String query, String category) {
        return searchRecipes(query, category).stream()
                .map(RecipeApiResponse::fromSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RecipeApiResponse> findAllRecipesApi(Pageable pageable) {
        return findAllRecipes(pageable).map(RecipeApiResponse::fromSummaryDTO);
    }
    public RecipeApiResponse getRecipeForApi(Long recipeId, Long userId) {
        RecipeResponseDTO recipe = findById(recipeId);
        boolean isFavorite = isFavorite(recipeId, userId);

        return RecipeApiResponse.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .category(recipe.getCategory())
                .preparationTime(recipe.getPreparationTime())
                .servings(recipe.getServings())
                .ingredients(recipe.getIngredients())
                .steps(recipe.getSteps())
                .createdAt(recipe.getCreatedAt())
                .coverImageUrl("/image?file=" + recipe.getCoverImagePath())
                .additionalImages(recipe.getAdditionalImages())
                .authorId(recipe.getUserId())
                .authorName(recipe.getAuthorName())
                .averageRating(recipe.getAverageRating())
                .isFavorite(isFavorite)
                .build();
    }


}