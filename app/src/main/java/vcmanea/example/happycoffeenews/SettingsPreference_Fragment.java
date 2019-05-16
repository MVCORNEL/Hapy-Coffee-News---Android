package vcmanea.example.happycoffeenews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import static vcmanea.example.happycoffeenews.PreferencesContract.CATEGORY_LIST;
import static vcmanea.example.happycoffeenews.PreferencesContract.LANGUAGE_LIST;
import static vcmanea.example.happycoffeenews.PreferencesContract.USER_NAME;


public class SettingsPreference_Fragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {


    private static final String TAG = "MainSettingsFragment";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Log.d(TAG, "onCreatePreferences: started");
        setPreferencesFromResource(R.xml.preferences, s);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sharedPreferences.edit();

        //****************USER******************//

        EditTextPreference editTextPreference = (EditTextPreference) findPreference("key_full_name");
        editTextPreference.setOnPreferenceChangeListener(this);

        //****************LIST PREFERENCES******************//

        ListPreference language = (ListPreference) findPreference("language_list");
        language.setOnPreferenceChangeListener(this);
        language.setDefaultValue("ro");

        ListPreference category = (ListPreference) findPreference("category_list");
        category.setOnPreferenceChangeListener(this);
        category.setDefaultValue("sports");

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference.getKey().equals(LANGUAGE_LIST)) {
            Log.d(TAG, "onPreferenceChange: " + o);
            saveDataLanguage(o.toString());
            preference.setDefaultValue(o);
            PreferencesContract.STATUS_CHANGED = true;
            return true;

        } else if (preference.getKey().equals(USER_NAME)) {
            Log.d(TAG, "onPreferenceChange: " + o);
            saveUserName(o.toString());
            preference.setSummary(o.toString());
            PreferencesContract.STATUS_CHANGED = true;
            OnlineNews.getNewsList().clear();
            return true;

        } else if (preference.getKey().equals(CATEGORY_LIST)) {

            Log.d(TAG, "onPreferenceChange: " + o);
            saveDataCatgory(o.toString());
            preference.setDefaultValue(o);
            PreferencesContract.STATUS_CHANGED = true;
            OnlineNews.getNewsList().clear();
            return true;

        }
        return false;

    }

    public void saveDataCatgory(String data) {
        editor.putString(CATEGORY_LIST, data);
        editor.apply();

    }

    public void saveDataLanguage(String data) {
        editor.putString(LANGUAGE_LIST, data);
        editor.apply();
    }

    public void saveUserName(String data) {

        editor.putString(USER_NAME, data);
        editor.apply();

    }


}



