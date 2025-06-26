package org.example.dto;

import lombok.Data;
import org.example.entity.Recipe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RecipeResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer preparationTime;
    private Integer servings;
    private String ingredients;
    private String steps;
    private String createdAt;
    private String coverImagePath;
    private List<String> additionalImages;
    private Long userId;
    private String authorName;
    private double averageRating;
    public String getFormattedCreatedAt() {
        if (createdAt == null) return null;
        LocalDateTime date = LocalDateTime.parse(createdAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return date.format(formatter);
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        map.put("category", category);
        map.put("preparationTime", preparationTime);
        map.put("servings", servings);
        map.put("ingredients", ingredients);
        map.put("steps", steps);
        map.put("createdAt", createdAt);
        map.put("coverImagePath", coverImagePath);
        map.put("additionalImages", additionalImages);
        map.put("userId", userId);
        map.put("authorName", authorName);
        map.put("averageRating", averageRating);
        return map;
    }

}
