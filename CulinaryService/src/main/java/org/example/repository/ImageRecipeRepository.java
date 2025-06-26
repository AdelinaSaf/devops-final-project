package org.example.repository;

import org.example.entity.ImageRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRecipeRepository extends JpaRepository<ImageRecipe, Long> {
    List<ImageRecipe> findByRecipeId(Long recipeId);

    @Modifying
    @Query("DELETE FROM ImageRecipe i WHERE i.recipe.id = :recipeId")
    void deleteByRecipeId(@Param("recipeId") Long recipeId);
}
