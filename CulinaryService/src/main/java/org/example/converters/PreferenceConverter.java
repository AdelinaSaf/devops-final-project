package org.example.converters;

import org.example.dto.PreferenceDTO;
import org.example.dto.PreferenceResponseDTO;
import org.example.entity.Preference;
import org.springframework.stereotype.Component;

@Component
public class PreferenceConverter {

    public Preference toEntity(PreferenceDTO dto) {
        Preference preference = new Preference();
        preference.setPreferenceName(dto.getName());
        return preference;
    }

    public PreferenceResponseDTO toResponseDTO(Preference preference) {
        return new PreferenceResponseDTO(preference.getId(), preference.getPreferenceName());
    }
}
