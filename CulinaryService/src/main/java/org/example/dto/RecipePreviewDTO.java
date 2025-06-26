package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipePreviewDTO {
    private Long id;
    private String name;
    private String category;
    private String coverImagePath;
    private String authorName;
    private String description;
    private int PreparationTime;
    private Integer servings;
    private LocalDateTime createdAt;
}
