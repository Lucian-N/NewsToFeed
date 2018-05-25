package com.example.lucian_pc.newstofeed;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class NewsSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    // Fragment to select and order by preferences
    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference newsNumberPreference = findPreference(getString(R.string.news_number));
            bindPreferenceSummaryToValue(newsNumberPreference);

            Preference sortByPreference = findPreference(getString(R.string.sort_by_key));
            bindPreferenceSummaryToValue(sortByPreference);

            Preference orderByPreference = findPreference(getString(R.string.order_by_key));
            bindPreferenceSummaryToValue(orderByPreference);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringPreference = value.toString();
            preference.setSummary(stringPreference);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}