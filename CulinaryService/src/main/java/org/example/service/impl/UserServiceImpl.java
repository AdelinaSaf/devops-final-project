package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.converters.UserConverter;
import org.example.dto.*;
import org.example.entity.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.AlreadyExistsException;
import org.example.repository.*;
import org.example.service.FileService;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final RecipeRepository recipeRepository;
    private final RatingRepository ratingRepository;
    private final UserConverter userConverter;
    private final UserFavoriteRecipeRepository userFavoriteRecipeRepository;

    @Override
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) throws AlreadyExistsException {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            log.warn("Попытка регистрации с существующим email: {}", registrationDTO.getEmail());
            throw new AlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            log.warn("Попытка регистрации с существующим именем: {}", registrationDTO.getUsername());
            throw new AlreadyExistsException("Username already in use");
        }
        if (registrationDTO.getPassword() == null || registrationDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        User user = userConverter.toEntity(registrationDTO);
        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user.setAvatar("default-avatar.jpg");

        User savedUser = userRepository.save(user);

        if (registrationDTO.getPreferenceIds() != null) {
            for (Long preferenceId : registrationDTO.getPreferenceIds()) {
                addPreferenceById(savedUser.getId(), preferenceId);
            }
        }

        return savedUser;
    }
    private void addPreferenceById(Long userId, Long preferenceId) {
        Preference preference = preferenceRepository.findById(preferenceId)
                .orElseThrow(() -> new EntityNotFoundException("Preference not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getPreferences().contains(preference)) {
            user.getPreferences().add(preference);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public UserProfileDTO updateUser(Long userId, UserUpdateDTO userDTO) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> {
                log.warn("Пользователь не найден: {}", userId);
                return new EntityNotFoundException("User not found");
            });

            userConverter.updateEntity(userDTO, user);


            if (userDTO.getAvatar() != null && !userDTO.getAvatar().isEmpty()) {
                String newAvatar = fileService.saveFile(userDTO.getAvatar());
                if (!"default-avatar.jpg".equals(user.getAvatar())) {
                    fileService.deleteFile(user.getAvatar());
                }
                user.setAvatar(newAvatar);
            }

            if (userDTO.getPreferences() != null) {
                userPreferenceRepository.deleteByUserId(userId);
                for (String preference : userDTO.getPreferences()) {
                    addPreferenceToUser(userId, new PreferenceDTO(preference));
                }
            }

            User updatedUser = userRepository.save(user);
            return toUserProfileDTO(updatedUser);
        } catch (IOException e) {
            log.error("Ошибка обновления аватара пользователя: {}", userId, e);
            throw new RuntimeException("Failed to update avatar", e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь не найден: {}", userId);
            return new EntityNotFoundException("User not found");
        });

        // Удаление аватара
        if (!"default-avatar.jpg".equals(user.getAvatar())) {
            fileService.deleteFile(user.getAvatar());
        }

        userRepository.delete(user);
    }

    @Override
    public UserProfileDTO findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarPath(user.getAvatar());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        dto.setCreatedAt(user.getCreatedAt().format(formatter));
        dto.setAdmin(user.isAdmin());

        dto.setPreferences(user.getPreferences().stream()
                .map(p -> new PreferenceDTO(p.getPreferenceName()))
                .collect(Collectors.toList()));

        dto.setCreatedRecipes(user.getRecipes().stream()
                .map(r -> new RecipePreviewDTO(r.getId(), r.getName(), r.getCoverImagePath(), r.getUser().getUsername(),
                        r.getCategory(), r.getDescription(), r.getPreparationTime(), r.getServings(), r.getCreatedAt()))
                .collect(Collectors.toList()));

        dto.setFavoriteRecipes(user.getFavoriteRecipes().stream()
                .map(r -> new RecipePreviewDTO(r.getId(), r.getName(), r.getCoverImagePath(), r.getUser().getUsername(),
                        r.getCategory(), r.getDescription(), r.getPreparationTime(), r.getServings(), r.getCreatedAt()))
                .collect(Collectors.toList()));

        dto.setComments(user.getComments().stream()
                .map(c -> new CommentDTO(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt().toString(),
                        c.getRecipe().getId(),
                        c.getRecipe().getName()))
                .collect(Collectors.toList()));

        dto.setCreatedRecipesCount(user.getRecipes().size());
        dto.setFavoriteRecipesCount(user.getFavoriteRecipes().size());
        dto.setCommentsCount(user.getComments().size());

        return dto;
    }

    @Override
    public UserProfileDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("Пользователь не найден: {}", email);
            return new EntityNotFoundException("User not found");
        });
        return toUserProfileDTO(user);
    }

    @Override
    public Page<UserProfileDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toUserProfileDTO);
    }

    private UserProfileDTO toUserProfileDTO(User user) {
        double rating = ratingRepository.calculateUserAverageRating(user.getId());

        List<PreferenceDTO> preferences = user.getPreferences().stream()
                .map(p -> new PreferenceDTO(p.getPreferenceName()))
                .collect(Collectors.toList());

        return userConverter.toProfileDTO(user, rating, preferences);
    }

    @Override
    public Page<UserProfileDTO> searchUsers(String query, Pageable pageable) {
        Page<User> users = userRepository.searchUsers(query, pageable);
        return users.map(this::toUserProfileDTO);
    }

    @Override
    @Transactional
    public UserProfileDTO grantOrRevokeAdminRights(AdminRightsDTO adminRightsDTO) {
        User user = userRepository.findById(adminRightsDTO.getUserId()).orElseThrow(() -> {
            log.warn("Пользователь не найден: {}", adminRightsDTO.getUserId());
            return new EntityNotFoundException("User not found");
        });
        user.setAdmin(adminRightsDTO.isGrant());
        User updatedUser = userRepository.save(user);
        return toUserProfileDTO(updatedUser);
    }

    @Override
    @Transactional
    public void addPreferenceToUser(Long userId, PreferenceDTO preferenceDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь не найден: {}", userId);
            return new EntityNotFoundException("User not found");
        });

        Preference preference = preferenceRepository.findByPreferenceName(preferenceDTO.getName()).orElseGet(() -> {
            Preference newPref = new Preference();
            newPref.setPreferenceName(preferenceDTO.getName());
            return preferenceRepository.save(newPref);
        });

        if (!user.getPreferences().contains(preference)) {
            user.getPreferences().add(preference);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void removePreferenceFromUser(Long userId, PreferenceDTO preferenceDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь не найден: {}", userId);
            return new EntityNotFoundException("User not found");
        });

        preferenceRepository.findByPreferenceName(preferenceDTO.getName()).ifPresent(pref -> {
            user.getPreferences().remove(pref);
            userRepository.save(user);
        });
    }

    @Override
    public List<String> getUserPreferences(Long userId) {
        return userPreferenceRepository.findPreferenceNamesByUserId(userId);
    }
    @Override
    @Transactional
    public void removeFromFavorites(Long recipeId, User user) {
        userFavoriteRecipeRepository.deleteByUserIdAndRecipeId(user.getId(), recipeId);
    }
}