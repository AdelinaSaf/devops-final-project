package org.example.converters;

import org.example.dto.PreferenceDTO;
import org.example.dto.UserProfileDTO;
import org.example.dto.UserRegistrationDTO;
import org.example.dto.UserUpdateDTO;
import org.example.entity.Preference;
import org.example.entity.User;
import org.example.service.PreferenceService;
import org.example.service.impl.PreferenceServiceImpl;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public User toEntity(UserRegistrationDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public void updateEntity(UserUpdateDTO dto, User user) {
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }

    }

    public UserProfileDTO toProfileDTO(User user, double rating, List<PreferenceDTO> preferences) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarPath(user.getAvatar());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dto.setCreatedAt(user.getCreatedAt().format(formatter));
        dto.setAdmin(user.isAdmin());
        dto.setUserRating(rating);
        dto.setPreferences(preferences);
        return dto;
    }
}