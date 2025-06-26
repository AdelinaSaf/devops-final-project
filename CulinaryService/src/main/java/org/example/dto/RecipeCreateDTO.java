package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class RecipeCreateDTO {
    @NotBlank(message = "Название рецепта обязательно")
    private String name;
    @NotBlank(message = "Описание обязательно")
    private String description;
    @NotBlank(message = "Категория обязательна")
    private String category;
    private Integer preparationTime;
    private Integer servings;
    private String ingredients;
    private String steps;
    private MultipartFile coverImage;
    private List<MultipartFile> images;
}
