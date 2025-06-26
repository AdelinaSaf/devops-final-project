package org.example.controllers;

import org.example.dto.RecipePreviewDTO;
import org.example.dto.RecipeSummaryDTO;
import org.example.dto.UserProfileDTO;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.PreferenceService;
import org.example.service.RecipeService;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cookbook")
public class CookbookController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final PreferenceService preferenceService;

    public CookbookController(RecipeService recipeService, UserService userService,PreferenceService preferenceService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.preferenceService = preferenceService;
    }

    @GetMapping
    public String showCookbook(@AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        UserProfileDTO userProfile = userService.findById(currentUser.getId());
        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());

        List<RecipePreviewDTO> recipes = recipeService.findAllByUserId(currentUser.getId());
        model.addAttribute("user", userProfile);
        model.addAttribute("recipes", recipes);
        model.addAttribute("preferences", preferences);
        return "cookbook";
    }


    @PostMapping("/delete")
    public String deleteRecipe(@RequestParam Long recipeId,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        recipeService.deleteRecipe(recipeId, currentUser.getId());
        return "redirect:/cookbook";
    }
}

