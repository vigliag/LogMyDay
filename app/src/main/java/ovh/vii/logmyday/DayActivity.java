package ovh.vii.logmyday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayActivity extends AppCompatActivity implements View.OnClickListener {

    List<Field> fieldList;
    String today;
    FieldController FC;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        Calendar c = GregorianCalendar.getInstance();
        today = Record.dateFormat.format(c.getTime());
        fieldList = Field.listAll(Field.class);

        List<Record> recordList =  Record.listAll(Record.class); //Record.find(Record.class, "date = ?", today);

        //index records by field_id
        Map<Long, Record> recordByField = new HashMap<>();
        for (Record r : recordList){
            recordByField.put(r.f_id, r);
        }

        //in memory join
        Map<Field, Record> recordsToHandle = new HashMap<>();
        for (Field f: fieldList) {
            Record r = recordByField.get(f.getId());
            if(r == null){
                r = new Record();
            }
            recordsToHandle.put(f,r);
        }

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        FC = new FieldController(recordsToHandle, container, this);
        FC.populateView(container);
        save =  (Button) findViewById(R.id.save_button);
        save.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
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
            Intent i = new Intent(this, FieldsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        FC.saveAll();
    }
}
