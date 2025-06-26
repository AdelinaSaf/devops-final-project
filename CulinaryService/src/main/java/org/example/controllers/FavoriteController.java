package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.FavoriteOperationDTO;
import org.example.entity.User;
import org.example.exceptions.EntityNotFoundException;
import org.example.security.CustomUserDetails;
import org.example.service.RecipeService;
import org.example.service.UserService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final RecipeService recipeService;
    private final UserService userService;

    public FavoriteController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String saveToFavorites(@RequestParam Long recipeId,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            throw new AuthenticationCredentialsNotFoundException("User not authenticated");
        }

        recipeService.addToFavorites(recipeId, userDetails.getUser());
        return "redirect:/recipe/" + recipeId;
    }
    @PostMapping("/remove")
    public String removeFromFavorites(@RequestParam Long recipeId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        recipeService.removeFromFavorites(recipeId, userDetails.getId());
        return "redirect:/recipe/" + recipeId;
    }
}