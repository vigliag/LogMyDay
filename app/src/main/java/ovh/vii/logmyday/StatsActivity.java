package ovh.vii.logmyday;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.orm.Database;
import com.orm.SugarApp;
import com.orm.SugarDb;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsActivity extends AppCompatActivity {
    public static final SimpleDateFormat labelFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        LineChart chart = (LineChart) findViewById(R.id.chart);
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        //TODO use manual joins, as SugarORM doesn't support them
        List<Field> datafields = Field.find(Field.class, "field_type = ?", String.valueOf(Field.VALUE_RECORD));

        Database sugardb = ((Application) SugarApp.getSugarContext()).obtainDatabase();
        SQLiteDatabase db = sugardb.getDB();

        //int daysAgo = 0;
        Calendar oldestTime = GregorianCalendar.getInstance();

        try {
            //get the minimum day as string
            Cursor mindayresult = db.rawQuery("Select MIN(day) as day from RECORD", null);
            mindayresult.moveToFirst();
            String minday = mindayresult.getString(0);

            //parse it as date
            Log.d("MI", "Oldest record is: " + minday);
            Date oldestdate = Record.dateFormat.parse(minday);


            //daysAgo = Days.daysBetween(new DateTime(oldestdate.getTime()), new DateTime()).getDays();

            oldestTime.setTime(oldestdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Log.d("MI", "Oldest record is " + daysAgo + " days old");

        Calendar today = GregorianCalendar.getInstance();

        //create a date to index map, and the list of labels
        Map<String, Integer> dateToIdx = new HashMap<>();
        ArrayList<String> labels = new ArrayList<>();

        int i=0;
        while((oldestTime.before(today))){
            dateToIdx.put(Record.dateFormat.format(oldestTime), i);
            labels.add(labelFormat.format(oldestTime));

            i++;
            oldestTime.add(Calendar.DATE, 1);
        }

        //Create the list of entries for each dataset
        List<LineDataSet> datasets = new ArrayList<>();

        for (Field f: datafields) {

            List<Entry> entries = new ArrayList<>();

            List<Record> recordsOfField = Record.findWithQuery(Record.class,
                    "Select * from RECORD where f_id = ? order by day", String.valueOf(f.getId()));

            for (Record r : recordsOfField) {
                entries.add(new Entry(r.getValue(), dateToIdx.get(r.getDay())));
            }

            LineDataSet ds = new LineDataSet(entries, f.name);
            datasets.add(ds);
        }

        LineData ld = new LineData(labels, datasets);
        chart.setData(ld);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
