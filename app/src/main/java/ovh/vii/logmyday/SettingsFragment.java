package ovh.vii.logmyday;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ovh.vii.logmyday.services.ReminderService;

/**
 * Created by vigliag on 9/17/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    //NOTE these must be the same as the ones set in preferences.xml
    public static final String KEY_PREF_SEND_NOTIFICATION = "pref_send_notification";
    public static final String KEY_PREF_PERSISTENT_NOTIFICATION = "pref_persistent_notification";
    public static final String KEY_PREF_NOTIFICATION_TIME = "pref_notification_time";
    public static final String KEY_PREF_ALLOW_SCROLLING_DAYS = "allow_scrolling_days";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(KEY_PREF_SEND_NOTIFICATION) || key.equals(KEY_PREF_NOTIFICATION_TIME)){

            boolean send_notification = sharedPreferences.getBoolean(KEY_PREF_PERSISTENT_NOTIFICATION, false);
            String notification_time = sharedPreferences.getString(KEY_PREF_NOTIFICATION_TIME, "21:00");

            if ( send_notification ) {

                //Parse time string to obtain hour and minute
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(notification_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);   // assigns calendar to given date
                int hours = calendar.get(Calendar.HOUR);
                int minutes = calendar.get(Calendar.MINUTE);

                scheduleReminder(hours,minutes);

            } else {

                cancelReminder();

            }
        }
    }

    public PendingIntent getPendingIntent(Activity activity){
        Intent myIntent = new Intent(activity, ReminderService.class);

        PendingIntent pendingIntent = PendingIntent.getService(activity,
                ReminderService.ALARM_ID,
                myIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        return pendingIntent;
    }

    public void cancelReminder(){
        Activity activity = getActivity();
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Activity.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(activity);
        alarmManager.cancel(pendingIntent);
    }

    public void scheduleReminder(int hour, int minutes){
        Activity activity = getActivity();
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Activity.ALARM_SERVICE);

        PendingIntent pendingIntent = getPendingIntent(activity);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 00);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);  //set repeating every 24 hours
        Toast.makeText(activity, "Reminder scheduled for: " + hour + ":" + minutes, Toast.LENGTH_LONG).show();
    }
}
