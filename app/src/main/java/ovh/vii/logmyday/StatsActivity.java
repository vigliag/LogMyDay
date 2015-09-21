package ovh.vii.logmyday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.vii.logmyday.data.Database;
import ovh.vii.logmyday.data.Field;
import ovh.vii.logmyday.data.Record;

public class StatsActivity extends AppCompatActivity {
    public static final SimpleDateFormat labelFormat = new SimpleDateFormat("MM/dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Database db = new Database();
        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Field> datafields = db.listFieldsContainingValues();

        Calendar oldestTime = db.getOldestRecordDate();

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

            List<Record> recordsOfField = db.listRecordsForField(f);


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
