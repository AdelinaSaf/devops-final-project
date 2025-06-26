package org.example.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.example.dto.CommentCreateDTO;
import org.example.dto.RatingDTO;
import org.example.dto.RecipeResponseDTO;
import org.example.dto.UserProfileDTO;
import org.example.entity.Recipe;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.CommentRatingService;
import org.example.service.RecipeService;
import org.example.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/recipe")
public class ShowRecipeController {

    private final RecipeService recipeService;
    private final CommentRatingService commentRatingService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    @Value("${pdf.service.url}")
    private String pdfServiceUrl;

    public ShowRecipeController(RecipeService recipeService,
                                CommentRatingService commentRatingService,
                                UserService userService,
                                RestTemplate restTemplate ) {
        this.recipeService = recipeService;
        this.commentRatingService = commentRatingService;
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{recipeId}")
    public String showRecipe(@PathVariable Long recipeId,
                             Model model,
                             @AuthenticationPrincipal CustomUserDetails currentUser) {
        RecipeResponseDTO recipe = recipeService.findById(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("comments", commentRatingService.getCommentsByRecipeId(recipeId));
        model.addAttribute("additionalImages", recipe.getAdditionalImages());
        boolean isFavorite = false;
        if (currentUser != null) {
            UserProfileDTO userProfile = userService.findById(currentUser.getId());
            model.addAttribute("user", userProfile);

            isFavorite = recipeService.isFavorite(recipeId, currentUser.getId());
        }
        model.addAttribute("isFavorite", isFavorite);
        if (currentUser != null) {
            UserProfileDTO userProfile = userService.findById(currentUser.getId());
            model.addAttribute("user", userProfile);
        }else {
            model.addAttribute("user", null);
        }

        return "showRecipe";
    }

    @PostMapping("/{recipeId}/comment")
    public String addComment(@PathVariable Long recipeId,
                             @RequestParam("commentText") String content,
                             @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        commentRatingService.addComment(recipeId, currentUser.getId(), content);
        return "redirect:/recipe/" + recipeId;
    }

    @PostMapping("/{recipeId}/rate")
    public String addRating(@PathVariable Long recipeId,
                            @RequestParam("rating") Double ratingValue,
                            @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        commentRatingService.addRating(recipeId, currentUser.getId(), ratingValue);
        return "redirect:/recipe/" + recipeId;
    }

    @PostMapping("/{recipeId}/delete")
    public String deleteRecipe(@PathVariable Long recipeId,
                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        recipeService.deleteRecipe(recipeId,currentUser.getId());
        return "redirect:/cookbook";
    }
    @PostMapping("/{id}/pdf")
    public ResponseEntity<Resource> generatePdf(@PathVariable Long id) {
        RecipeResponseDTO recipe = recipeService.findById(id);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(recipe.toMap(), headers);

        byte[] pdfBytes = restTemplate.postForObject(pdfServiceUrl, request, byte[].class);

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=recipe.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
