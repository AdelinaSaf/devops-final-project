package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.UserRegistrationDTO;
import org.example.entity.Preference;
import org.example.exceptions.AlreadyExistsException;
import org.example.service.PreferenceService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final PreferenceService preferenceService;
    private final UserService userService;

    public RegisterController(PreferenceService preferenceService, UserService userService) {
        this.preferenceService = preferenceService;
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        // Убрать преобразование в строки!
        List<Preference> preferences = preferenceService.getAllPreferences();
        model.addAttribute("user", new UserRegistrationDTO());
        model.addAttribute("preferences", preferences);
        return "register";
    }
    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO registrationDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("preferences", preferenceService.getAllPreferences());
            return "register";
        }

        try {
            userService.registerUser(registrationDTO);
            return "redirect:/login";
        } catch (AlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("preferences", preferenceService.getAllPreferences());
            return "register";
        }
    }
}
