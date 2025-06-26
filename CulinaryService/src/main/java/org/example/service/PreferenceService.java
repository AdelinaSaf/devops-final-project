package org.example.service;

import org.example.entity.Preference;
import java.util.List;

public interface PreferenceService {
    Preference createPreference(String name);
    void deletePreference(String name);
    List<Preference> getAllPreferences();
    public List<String> getAllPreferenceNames();
}
