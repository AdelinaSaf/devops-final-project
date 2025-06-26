package org.example.controllers;

import org.example.dto.RecipeApiResponse;
import org.example.dto.RecipeResponseDTO;
import org.example.dto.RecipeSummaryDTO;
import org.example.security.CustomUserDetails;
import org.example.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeApiController {

    private final RecipeService recipeService;

    public RecipeApiController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable("id") Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        recipeService.addToFavorites(recipeId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable("id") Long recipeId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        recipeService.removeFromFavorites(recipeId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeSummaryDTO>> searchRecipes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(recipeService.searchRecipes(query, category));
    }

}
