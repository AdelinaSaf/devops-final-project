package org.example.repository;

import org.example.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByPreferenceName(String preferenceName);

    boolean existsByPreferenceName(String preferenceName);
}
