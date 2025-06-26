package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.converters.PreferenceConverter;
import org.example.dto.PreferenceDTO;
import org.example.exceptions.AlreadyExistsException;
import org.example.exceptions.EntityNotFoundException;
import org.example.service.PreferenceService;
import org.example.repository.PreferenceRepository;
import org.example.entity.Preference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final PreferenceConverter preferenceConverter;

    @Override
    @Transactional
    public Preference createPreference(String name) {
        if (preferenceRepository.existsByPreferenceName(name)) {
            log.warn("Попытка создания существующего предпочтения: {}", name);
            throw new AlreadyExistsException("Preference already exists");
        }

        PreferenceDTO dto = new PreferenceDTO(name);
        return preferenceRepository.save(preferenceConverter.toEntity(dto));
    }

    @Override
    @Transactional
    public void deletePreference(String name) {
        Preference preference = preferenceRepository.findByPreferenceName(name)
                .orElseThrow(() -> {
                    log.warn("Предпочтение не найдено: {}", name);
                    return new EntityNotFoundException("Preference not found");
                });
        preferenceRepository.delete(preference);
    }

    @Override
    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }
    @Override
    public List<String> getAllPreferenceNames() {
        return preferenceRepository.findAll().stream()
                .map(Preference::getPreferenceName)
                .collect(Collectors.toList());
    }
}