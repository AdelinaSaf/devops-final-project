package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.example.entity.Rating;
import org.example.entity.Recipe;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRecipeRepositoryImpl implements CustomRecipeRepository {

    private final EntityManager entityManager;

    @Override
    public List<Recipe> findTopRatedRecipes(int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
        Root<Recipe> recipe = query.from(Recipe.class);

        Subquery<Double> avgRatingSubquery = query.subquery(Double.class);
        Root<Rating> rating = avgRatingSubquery.from(Rating.class);
        avgRatingSubquery.select(cb.avg(rating.get("rating")))
                .where(cb.equal(rating.get("recipe"), recipe));

        query.select(recipe)
                .orderBy(cb.desc(avgRatingSubquery))
                .distinct(true);

        return entityManager.createQuery(query)
                .setMaxResults(limit)
                .getResultList();
    }
}
