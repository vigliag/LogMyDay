package ovh.vii.logmyday;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

            EditText value = (EditText) container.findViewWithTag(f.getId());

            if(f.istext){
                r.setText(value.getText().toString());
            } else {
                r.setValue(Integer.parseInt(value.getText().toString()));
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

            EditText value = new EditText(ctx);
            value.setTag(f.getId());

            if(!f.istext)
                value.setInputType(InputType.TYPE_CLASS_NUMBER);

            value.setLayoutParams(lparams);
            container.addView(value);
        }
    }
}
