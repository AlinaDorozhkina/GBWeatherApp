package ru.alinadorozhkina.gbweatherapp.fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import ru.alinadorozhkina.gbweatherapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_settings);

    }
}
