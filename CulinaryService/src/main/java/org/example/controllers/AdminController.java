package org.example.controllers;

import org.example.dto.AdminRightsDTO;
import org.example.dto.UserProfileDTO;
import org.example.entity.User;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.PreferenceService;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PreferenceService preferenceService;
    private final UserService userService;

    public AdminController(PreferenceService preferenceService, UserService userService) {
        this.preferenceService = preferenceService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model,
                            @RequestParam(required = false) String query,
                            Pageable pageable) {
        List<String> preferences = preferenceService.getAllPreferences().stream()
                .map(p -> p.getPreferenceName())
                .collect(Collectors.toList());
        model.addAttribute("preferences", preferences);

        Page<UserProfileDTO> users;
        if (query != null && !query.isEmpty()) {
            users = userService.searchUsers(query, pageable);
        } else {
            users = userService.findAllUsers(pageable);
        }
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String newCategory) throws AlreadyExistsException {
        preferenceService.createPreference(newCategory);
        return "redirect:/admin";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam String deleteCategoryName) throws EntityNotFoundException {
        preferenceService.deletePreference(deleteCategoryName);
        return "redirect:/admin";
    }

    @PostMapping("/grantRights")
    public String grantRights(@RequestParam Long userId) {
        AdminRightsDTO dto = new AdminRightsDTO();
        dto.setUserId(userId);
        dto.setGrant(true);
        userService.grantOrRevokeAdminRights(dto);
        return "redirect:/admin";
    }

    @PostMapping("/revokeRights")
    public String revokeRights(@RequestParam Long userId) {
        AdminRightsDTO dto = new AdminRightsDTO();
        dto.setUserId(userId);
        dto.setGrant(false);
        userService.grantOrRevokeAdminRights(dto);
        return "redirect:/admin";
    }
}