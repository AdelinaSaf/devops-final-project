package org.example.repository;


import org.example.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.recipe.id = :recipeId")
    Double calculateAverageRating(@Param("recipeId") Long recipeId);

    @Query("SELECT r FROM Rating r WHERE r.recipe.id IN " +
            "(SELECT rec.id FROM Recipe rec WHERE rec.user.id = :userId)")
    List<Rating> findRatingsForUserRecipes(@Param("userId") Long userId);

    @Query("SELECT COALESCE(AVG(r.rating), 0.0) FROM Rating r " +
            "WHERE r.recipe IN (SELECT rec FROM Recipe rec WHERE rec.user.id = :userId)")
    Double calculateUserAverageRating(@Param("userId") Long userId);
    void deleteByRecipeId(Long recipeId);
}
