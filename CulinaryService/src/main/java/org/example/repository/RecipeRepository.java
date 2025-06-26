package org.example.repository;

import org.example.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUserId(Long userId);

    @Query("SELECT r FROM Recipe r WHERE r.id IN " +
            "(SELECT f.recipe.id FROM UserFavoriteRecipe f WHERE f.user.id = :userId)")
    List<Recipe> findFavoriteRecipesByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Recipe r WHERE " +
            "(:query IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "(:category IS NULL OR r.category = :category)")
    List<Recipe> searchRecipes(@Param("query") String query,
                               @Param("category") String category);

}
