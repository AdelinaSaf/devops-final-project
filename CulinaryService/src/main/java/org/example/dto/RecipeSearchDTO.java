package org.example.dto;

import lombok.Data;
import org.example.entity.Recipe;

@Data
public class RecipeSearchDTO {
    private String query;
    private String  category;
    private Integer maxCookingTime;
}
