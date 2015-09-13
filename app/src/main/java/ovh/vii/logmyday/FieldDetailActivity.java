package ovh.vii.logmyday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class FieldDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Switch is_text;
    EditText name;
    EditText max_value;
    Button save_button;
    Field f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        Intent intent = getIntent();
        long field_id = intent.getLongExtra(FieldsActivity.FIELD_ID, -1);

        if(field_id == -1){
           f = new Field();
        } else {
           f = Field.findById(Field.class, field_id);
        }

        is_text = (Switch) findViewById(R.id.is_text);
        is_text.setChecked(f.istext);

        name = (EditText) findViewById(R.id.name);
        name.setText(f.name);

        max_value = (EditText) findViewById(R.id.max_value);
        max_value.setText("" + f.maxvalue);

        Button save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_field_detail, menu);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        f.istext = is_text.isChecked();
        f.maxvalue = Integer.parseInt(max_value.getText().toString());
        f.name = name.getText().toString();
        f.save();
        Toast.makeText(this,"Field saved", Toast.LENGTH_LONG);
    }
}
