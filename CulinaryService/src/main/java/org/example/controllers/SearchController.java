package org.example.controllers;

import org.example.dto.RecipeSummaryDTO;
import org.example.service.PreferenceService;
import org.example.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final RecipeService recipeService;
    private final PreferenceService preferenceService;

    public SearchController(RecipeService recipeService, PreferenceService preferenceService) {
        this.recipeService = recipeService;
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public String searchRecipes(@RequestParam(required = false) String query,
                                @RequestParam(required = false) String category,
                                Model model) {
        List<String> preferences = preferenceService.getAllPreferenceNames();
        model.addAttribute("preferences", preferences);

        List<RecipeSummaryDTO> recipes = recipeService.searchRecipes(
                query, category
        );
        model.addAttribute("recipes", recipes);
        return "search";
    }
}