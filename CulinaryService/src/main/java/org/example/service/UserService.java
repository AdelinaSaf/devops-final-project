package org.example.service;

import org.example.dto.UserRegistrationDTO;
import org.example.dto.UserProfileDTO;
import org.example.dto.UserUpdateDTO;
import org.example.dto.AdminRightsDTO;
import org.example.dto.PreferenceDTO;
import org.example.entity.User;
import org.example.exceptions.AlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO) throws AlreadyExistsException;
    UserProfileDTO updateUser(Long userId, UserUpdateDTO userDTO);
    void deleteUser(Long userId);
    UserProfileDTO findById(Long userId);
    UserProfileDTO findByEmail(String email);
    Page<UserProfileDTO> findAllUsers(Pageable pageable);
    Page<UserProfileDTO> searchUsers(String query, Pageable pageable);

    UserProfileDTO grantOrRevokeAdminRights(AdminRightsDTO adminRightsDTO);
    void addPreferenceToUser(Long userId, PreferenceDTO preferenceDTO);
    void removePreferenceFromUser(Long userId, PreferenceDTO preferenceDTO);
    List<String> getUserPreferences(Long userId);
    void removeFromFavorites(Long recipeId, User user);

}