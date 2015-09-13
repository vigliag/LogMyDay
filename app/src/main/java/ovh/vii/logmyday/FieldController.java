package ovh.vii.logmyday;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Map;

/**
 * Created by vigliag on 9/12/15.
 */
public class FieldController {

    private final Context ctx;
    private final LinearLayout container;
    private Map<Field, Record> recordsToHandle;
    private static final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public FieldController(Map<Field, Record> recordsToHandle, LinearLayout container, Context ctx) {
        this.recordsToHandle = recordsToHandle;
        this.ctx = ctx;
        this.container = container;
    }

    public void saveAll() {
        for (Map.Entry<Field, Record> entry : recordsToHandle.entrySet()) {
            Field f = entry.getKey();
            Record r = entry.getValue();

            View value = container.findViewWithTag(f.getId());

            if(f.fieldType == Field.TEXT_RECORD){
                EditText ed = (EditText) value;
                r.setText(ed.getText().toString());

            } else if(f.fieldType == Field.VALUE_RECORD) {
                DiscreteSeekBar dsb = (DiscreteSeekBar) value;
                r.setValue(dsb.getProgress());
            }

            r.save();
        }
    }

    public void populateView(LinearLayout container) {

        for (Map.Entry<Field, Record> entry : recordsToHandle.entrySet()) {
            Field f = entry.getKey();
            Record r = entry.getValue();

            TextView name = new TextView(ctx);
            name.setText(f.name);
            name.setLayoutParams(lparams);
            container.addView(name);

            View value;


            if(f.fieldType == Field.TEXT_RECORD) {
                EditText ed = new EditText(ctx);
                ed.setText(r.getText());
                value = ed;
            } else {
                DiscreteSeekBar dsb = new DiscreteSeekBar(ctx);
                dsb.setMax(f.maxvalue);
                dsb.setMin(f.minvalue);
                dsb.setProgress(r.getValue());
                value = dsb;
            }

            value.setTag(f.getId());
            value.setLayoutParams(lparams);
            container.addView(value);
        }
    }
}
