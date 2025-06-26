package org.example.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.RecipeCreateDTO;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.PreferenceService;
import org.example.service.RecipeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/recipe/create")
public class CreateRecipeController {

    private final PreferenceService preferenceService;
    private final RecipeService recipeService;

    public CreateRecipeController(PreferenceService preferenceService, RecipeService recipeService) {
        this.preferenceService = preferenceService;
        this.recipeService = recipeService;
    }

    @GetMapping
    public String showCreateForm(Model model) {
        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());
        model.addAttribute("preferences", preferences);
        return "createRecipe";
    }

    @PostMapping
    public String createRecipe(@Valid @ModelAttribute RecipeCreateDTO recipeDTO,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal CustomUserDetails customUserDetails,
                               Model model) {

        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());
        model.addAttribute("preferences", preferences);

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при создании рецепта: {}", bindingResult.getAllErrors());
            return "createRecipe";
        }

        try {
            Long userId = customUserDetails.getId();
            recipeService.createRecipe(recipeDTO, userId);
            return "redirect:/cookbook";
        } catch (Exception e) {
            log.error("Ошибка создания рецепта", e);
            model.addAttribute("error", "Ошибка создания рецепта: " + e.getMessage());
            return "createRecipe";
        }
    }
}
