package ovh.vii.logmyday.activities;

import android.app.Activity;
import android.os.Bundle;

import ovh.vii.logmyday.SettingsFragment;

/**
 * Created by vigliag on 9/17/15.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
