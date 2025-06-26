package org.example.repository;


import org.example.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    @Query("SELECT p.preference.preferenceName FROM UserPreference p WHERE p.user.id = :userId")
    List<String> findPreferenceNamesByUserId(@Param("userId") Long userId);

    void deleteByUserId(Long userId);

    @Query("SELECT up FROM UserPreference up WHERE up.user.id IN " +
            "(SELECT u.id FROM User u WHERE u.isAdmin = true)")
    List<UserPreference> findPreferencesForAdmins();
}
