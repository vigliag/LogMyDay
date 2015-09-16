package ovh.vii.logmyday;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by vigliag on 9/17/15.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
