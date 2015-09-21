package ovh.vii.logmyday;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import ovh.vii.logmyday.activities.FieldManagerActivity;
import ovh.vii.logmyday.data.Field;
import ovh.vii.logmyday.data.Record;

public class FieldDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Switch is_text;
    EditText name;
    EditText max_value;
    Button delete_button;
    Button save_button;
    Field f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        Intent intent = getIntent();
        long field_id = intent.getLongExtra(FieldManagerActivity.FIELD_ID, -1);

        if(field_id == -1){
           f = new Field();
        } else {
           f = Field.findById(Field.class, field_id);
        }

        is_text = (Switch) findViewById(R.id.is_text);
        is_text.setChecked(f.getFieldType() == Field.TEXT_RECORD);

        name = (EditText) findViewById(R.id.name);
        name.setText(f.getName());

        max_value = (EditText) findViewById(R.id.max_value);
        max_value.setText("" + f.getMaxvalue());

        save_button = (Button) findViewById(R.id.save_button);
        save_button.setOnClickListener(this);

        delete_button = (Button) findViewById(R.id.delete_button);
        if(f.getId() != null){
            delete_button.setEnabled(true);
            delete_button.setOnClickListener(this);
        }

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

        if(v.getId() == R.id.save_button){
            f.setFieldType(is_text.isChecked() ? Field.TEXT_RECORD : Field.VALUE_RECORD);
            f.setMaxvalue(Integer.parseInt(max_value.getText().toString()));
            f.setName(name.getText().toString());
            f.save();
            Toast.makeText(this,"Field saved", Toast.LENGTH_LONG).show();
            finish();
        }

        if(v.getId() == R.id.delete_button){
            new AlertDialog.Builder(FieldDetailActivity.this)
                    .setTitle("Are you sure?")
                    .setMessage("Deleting this field, all associated records will be deleted as well.")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Record.deleteAll(Record.class, "fid = ?", String.valueOf(f.getId()));
                            f.delete();
                            Toast.makeText(FieldDetailActivity.this, "Field and associated records deleted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //pass
                        }
                    })
                    .show();
        }

    }
}
