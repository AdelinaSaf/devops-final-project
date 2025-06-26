package org.example.controllers;

import org.example.dto.UserProfileDTO;
import org.example.dto.UserUpdateDTO;
import org.example.entity.User;
import org.example.security.CustomUserDetails;
import org.example.service.PreferenceService;
import org.example.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile/edit")
public class EditProfileController {

    private final PreferenceService preferenceService;
    private final UserService userService;

    public EditProfileController(PreferenceService preferenceService, UserService userService) {
        this.preferenceService = preferenceService;
        this.userService = userService;
    }

    @GetMapping
    public String showEditForm(@AuthenticationPrincipal CustomUserDetails currentUser,
                               Model model) {
        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());
        model.addAttribute("preferences", preferences);

        UserProfileDTO profile = userService.findById(currentUser.getId());
        model.addAttribute("user", profile);
        return "editProfile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute UserUpdateDTO userDTO,
                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        userService.updateUser(currentUser.getId(), userDTO);
        return "redirect:/profile/" + currentUser.getId();
    }
}