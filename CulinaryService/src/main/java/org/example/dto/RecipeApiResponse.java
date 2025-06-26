package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeApiResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String coverImageUrl;
    private List<String> additionalImages; // Изменили тип
    private Long authorId; // Добавили поле
    private String authorName;
    private double averageRating;
    private Integer preparationTime;
    private Integer servings;
    private String ingredients;
    private String steps;
    private String createdAt;
    private Boolean isFavorite; // Добавили поле


    public static RecipeApiResponse fromSummaryDTO(RecipeSummaryDTO dto) {
        return RecipeApiResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .coverImageUrl("/image?file=" + dto.getCoverImagePath())
                .authorName(dto.getAuthorName())
                .averageRating(dto.getAverageRating())
                .preparationTime(dto.getPreparationTime())
                .build();
    }
}