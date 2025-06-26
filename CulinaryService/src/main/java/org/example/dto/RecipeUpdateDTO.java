package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.entity.Recipe;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RecipeUpdateDTO {
    private Long id;
    @NotBlank(message = "Название обязательно")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @NotBlank(message = "Категория обязательна")
    private String category;

    @NotNull(message = "Время приготовления обязательно")
    private Integer preparationTime;

    @NotNull(message = "Количество порций обязательно")
    private Integer servings;

    @NotBlank(message = "Ингредиенты обязательны")
    private String ingredients;

    @NotBlank(message = "Шаги приготовления обязательны")
    private String steps;
    private MultipartFile coverImage;
    private List<MultipartFile> images;
}
