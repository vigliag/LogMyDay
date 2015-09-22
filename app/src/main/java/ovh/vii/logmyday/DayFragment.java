package ovh.vii.logmyday;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import ovh.vii.logmyday.data.Database;
import ovh.vii.logmyday.data.Field;
import ovh.vii.logmyday.data.Record;

/**
 * Shows the records for a given day_string passed as param
 * handles modifying and saving groups of records
 * the actual logic of creating and handling the views for every record is delegated to the FieldController
 */
public class DayFragment extends Fragment implements View.OnClickListener {
    private static final String DAY_PARAM = "param1";

    private String day_string;
    private FieldController FC;
    private Button save;
    private Database db;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param day Parameter 1.
     * @return A new instance of fragment DayFragment.
     */
    public static DayFragment newInstance(String day) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(DAY_PARAM, day);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day_string = getArguments().getString(DAY_PARAM);
        }
        db = new Database();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_day, container, false);

        TextView dayDisplay = (TextView) layout.findViewById(R.id.day);

        String displayableDate = day_string;

        //obtain displayable date
        try {
            Date d = Record.dateFormat.parse(day_string);
            DateFormat df = DateFormat.getDateInstance();
            displayableDate = df.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dayDisplay.setText(displayableDate);

        Map<Field, Record> recordsToHandle = db.recordsPerFieldForDay(day_string);

        LinearLayout fieldsContainer = (LinearLayout) layout.findViewById(R.id.container);

        FC = new FieldController(recordsToHandle, fieldsContainer, getActivity());
        FC.populateView(fieldsContainer);

        save =  (Button) layout.findViewById(R.id.save_button);
        save.setOnClickListener(this);

        return layout;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        FC.saveAll();
        Toast.makeText(getActivity(), "Changes saved", Toast.LENGTH_LONG).show();
    }
}
