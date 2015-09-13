package ovh.vii.logmyday;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Intent;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int NDAYS = 5;

    DayPagerAdapter mDayPagerAdapter;
    ViewPager mViewPager;
    Calendar today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        today = GregorianCalendar.getInstance();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mDayPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDayPagerAdapter);
        mViewPager.setCurrentItem(NDAYS);
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
            Intent i = new Intent(this, FieldsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_stats) {
            Intent i = new Intent(this, StatsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            mCalendarDay.add(GregorianCalendar.DATE, position - NDAYS +1);

            String day = Record.dateFormat.format(mCalendarDay.getTime());

            return DayFragment.newInstance(day);
        }

        @Override
        public int getCount() {
            return NDAYS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

}
