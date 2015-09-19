package ovh.vii.logmyday;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ovh.vii.logmyday.activities.FieldManagerActivity;
import ovh.vii.logmyday.activities.SettingsActivity;
import ovh.vii.logmyday.services.ReminderService;

public class MainActivity extends AppCompatActivity {

    private static final int NDAYS = 10;
    private int days_to_show = NDAYS;

    private boolean pageHasToBeRefreshed = false;

    DayPagerAdapter mDayPagerAdapter;
    ViewPager mViewPager;
    Calendar today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancelNotifications();

        initPage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cancelNotifications();

        if(pageHasToBeRefreshed){
            initPage();
        }
    }

    protected void cancelNotifications(){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(ReminderService.NOTIFICATION_ID);
    }

    protected void initPage(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean allow_scrolling = preferences.
                getBoolean(SettingsFragment.KEY_PREF_ALLOW_SCROLLING_DAYS, false);

        if(allow_scrolling){
            days_to_show = NDAYS;
        } else {
            days_to_show = 1;
        }

        today = GregorianCalendar.getInstance();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDayPagerAdapter);
        mViewPager.setCurrentItem(days_to_show);
        pageHasToBeRefreshed = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            pageHasToBeRefreshed = true;
            startActivityForResult(i, 1);
            return true;
        }

        if (id == R.id.action_stats) {
            Intent i = new Intent(this, StatsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_export ){
            new AlertDialog.Builder(this)
                    .setTitle("Exporting records")
                    .setMessage("Records will be exported in your download folder with today's date")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            exportToFile();
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void exportToFile(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        String curdate = Record.dateFormat.format(GregorianCalendar.getInstance().getTime());
        File file = new File(path, "ExportedRecords " + curdate +".csv");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            List<Record> records = Record.listAll(Record.class);

            for(Record record: records){
                writer.write(record.toCSV() + "\n");
            }

            writer.close();

            Toast.makeText(this, "Records exported in download folder", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class DayPagerAdapter extends FragmentStatePagerAdapter {

        public DayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Calendar mCalendarDay = new GregorianCalendar().getInstance(today.getTimeZone());
            mCalendarDay.setTime(today.getTime());
            mCalendarDay.add(GregorianCalendar.DATE, position - days_to_show +1);

            String day = Record.dateFormat.format(mCalendarDay.getTime());

            return DayFragment.newInstance(day);
        }

        @Override
        public int getCount() {
            return days_to_show;
        }

    }

}
