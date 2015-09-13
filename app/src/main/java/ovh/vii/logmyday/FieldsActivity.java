package ovh.vii.logmyday;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class FieldsActivity extends AppCompatActivity {

    public static final String FIELD_ID = "field_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields);

        List<Field> fields = Field.listAll(Field.class);
        ListView listView = (ListView) findViewById(R.id.listView);

        Button add_new_button = (Button) findViewById(R.id.add_new_button);
        add_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FieldsActivity.this, FieldDetailActivity.class);
                startActivity(i);
            }
        });

        listView.setAdapter(
                new ArrayAdapter<Field>(
                        this,
                        android.R.layout.simple_list_item_1,
                        fields)
        );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Field field = (Field) parent.getItemAtPosition(position);
                Intent i = new Intent(FieldsActivity.this,
                        FieldDetailActivity.class);
                i.putExtra(FIELD_ID, field.getId());
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fields, menu);
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
