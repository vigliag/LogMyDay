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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orm.Database;
import com.orm.SugarApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.vii.logmyday.data.Field;
import ovh.vii.logmyday.data.Record;

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

            if(minday != null){
                Date oldestdate = Record.dateFormat.parse(minday);
                oldestTime.setTime(oldestdate);
            }

            //daysAgo = Days.daysBetween(new DateTime(oldestdate.getTime()), new DateTime()).getDays();

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
            dateToIdx.put(Record.dateFormat.format(oldestTime.getTime()), i);
            labels.add(labelFormat.format(oldestTime.getTime()));

            i++;
            oldestTime.add(Calendar.DATE, 1);
        }

        //Create the list of entries for each dataset
        List<LineDataSet> datasets = new ArrayList<>();

        //TODO let user specify color
        //TODO use a larger set of colors
        int colorIndex= 0;

        for (Field f: datafields) {

            List<Entry> entries = new ArrayList<>();

            List<Record> recordsOfField = Record.findWithQuery(Record.class,
                    "Select * from RECORD where fid = ? order by day", String.valueOf(f.getId()));

            for (Record r : recordsOfField) {
                entries.add(new Entry(r.getValue(), dateToIdx.get(r.getDay())));
            }

            LineDataSet ds = new LineDataSet(entries, f.getName());

            //styling
            ds.setCircleSize(5f);
            ds.setColor(ColorTemplate.PASTEL_COLORS[colorIndex]);
            ds.setCircleColor(ColorTemplate.PASTEL_COLORS[colorIndex]);
            colorIndex = (colorIndex + 1) % ColorTemplate.PASTEL_COLORS.length;

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
