package org.example.dto;

import lombok.Data;
import org.example.entity.Recipe;

@Data
public class RecipeSummaryDTO {
    private Long id;
    private String name;
    private String category;
    private String description;
    private Integer preparationTime;
    private String coverImagePath;
    private String authorName;
    private double averageRating;
}
