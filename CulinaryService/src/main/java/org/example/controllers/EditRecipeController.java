package org.example.controllers;

import org.example.dto.RecipeResponseDTO;
import org.example.dto.RecipeUpdateDTO;
import org.example.entity.User;
import org.example.exceptions.AccessDeniedException;
import org.example.exceptions.OperationNotAllowedException;
import org.example.security.CustomUserDetails;
import org.example.service.FileService;
import org.example.service.PreferenceService;
import org.example.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipe/edit")
public class EditRecipeController {

    private final RecipeService recipeService;
    private final PreferenceService preferenceService;
    private final FileService fileService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public EditRecipeController(RecipeService recipeService,
                                PreferenceService preferenceService,
                                FileService fileService) {
        this.recipeService = recipeService;
        this.preferenceService = preferenceService;
        this.fileService = fileService;
    }

    @GetMapping("/{recipeId}")
    public String showEditForm(@PathVariable Long recipeId,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        RecipeResponseDTO recipe = recipeService.findById(recipeId);

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (!recipe.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this recipe");
        }

        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());
        Set<String> selectedCategories = new HashSet<>(Arrays.asList(recipe.getCategory().split(",")));
        model.addAttribute("preferences", preferences);
        model.addAttribute("selectedCategories", selectedCategories);
        model.addAttribute("recipe", recipe);
        return "editRecipe";
    }

    @PostMapping("/{recipeId}")
    public String updateRecipe(@PathVariable Long recipeId,
                               @ModelAttribute RecipeUpdateDTO recipeDTO,
                               BindingResult bindingResult,
                               @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                               @RequestParam(value = "images", required = false) List<MultipartFile> images,
                               @AuthenticationPrincipal User currentUser,
                               Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            RecipeResponseDTO recipe = recipeService.findById(recipeId);
            model.addAttribute("recipe", recipe);
            model.addAttribute("preferences", preferenceService.getAllPreferences().stream()
                    .map(p -> p.getPreferenceName())
                    .collect(Collectors.toList()));
            return "editRecipe";
        }
        if (coverImage != null) {
            recipeDTO.setCoverImage(coverImage);
        }
        if (images != null) {
            recipeDTO.setImages(images);
        }

        recipeService.updateRecipe(recipeId, recipeDTO);
        return "redirect:/recipe/" + recipeId;
    }
}