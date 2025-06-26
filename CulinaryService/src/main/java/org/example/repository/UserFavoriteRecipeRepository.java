package org.example.repository;

import org.example.entity.UserFavoriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRecipeRepository extends JpaRepository<UserFavoriteRecipe, Long> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query("SELECT f.id FROM UserFavoriteRecipe f WHERE f.user.id = :userId")
    List<Long> findFavoriteRecipeIdsByUserId(@Param("userId") Long userId);
    void deleteByRecipeId(Long recipeId);
}
