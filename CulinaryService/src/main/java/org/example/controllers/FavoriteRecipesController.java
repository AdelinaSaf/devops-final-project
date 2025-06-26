package org.example.controllers;

import org.example.dto.FavoriteOperationDTO;
import org.example.dto.RecipeSummaryDTO;
import org.example.dto.UserProfileDTO;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.PreferenceService;
import org.example.service.RecipeService;
import org.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favoriteRecipes")
public class FavoriteRecipesController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final PreferenceService preferenceService;

    public FavoriteRecipesController(RecipeService recipeService, UserService userService, PreferenceService preferenceService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public String showFavorites(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        UserProfileDTO userProfile = userService.findById(currentUser.getId());
        model.addAttribute("user", userProfile);

        List<RecipeSummaryDTO> favoriteRecipes = recipeService.findFavoriteRecipes(currentUser.getId());
        model.addAttribute("favoriteRecipes", favoriteRecipes);

        model.addAttribute("preferences", preferenceService.getAllPreferenceNames());

        return "favoriteRecipes";
    }

}