package org.example.repository;

import org.example.entity.Recipe;

import java.util.List;

public interface CustomRecipeRepository {
    List<Recipe> findTopRatedRecipes(int limit);
}