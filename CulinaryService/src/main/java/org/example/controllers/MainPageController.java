package org.example.controllers;

import org.example.dto.RecipeSummaryDTO;
import org.example.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainPageController {

    private final RecipeService recipeService;

    public MainPageController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public String showMainPage(Model model, Pageable pageable) {
        Page<RecipeSummaryDTO> recipesPage = recipeService.findAllRecipes(pageable);
        model.addAttribute("recipes", recipesPage.getContent());
        model.addAttribute("totalPages", recipesPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        return "mainPage";
    }
}