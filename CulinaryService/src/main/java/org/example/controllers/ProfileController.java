package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.UserProfileDTO;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public String showProfile(@PathVariable Long userId,
                              Model model,
                              @AuthenticationPrincipal CustomUserDetails currentUser) {
        UserProfileDTO profile = userService.findById(userId);
        model.addAttribute("profileUser", profile);

        if (currentUser != null) {
            model.addAttribute("currentUserId", currentUser.getId());
        }

        return "profile";
    }

    @PostMapping("/{id}/delete")
    public String deleteProfile(@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserDetails currentUserDetails,
                                HttpServletRequest request) {
        if (currentUserDetails.getId().equals(id)) {
            userService.deleteUser(id);
            return "redirect:/logout";
        }
        return "redirect:/accessDenied";
    }

}
