package ovh.vii.logmyday;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayFragment extends Fragment implements View.OnClickListener {
    private static final String DAY_PARAM = "param1";

    private String day;
    List<Field> fieldList;
    FieldController FC;
    Button save;

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

    public DayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString(DAY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_day, container, false);

        TextView dayDisplay = (TextView) layout.findViewById(R.id.day);
        dayDisplay.setText("Day " + day);

        fieldList = Field.listAll(Field.class);
        List<Record> recordList = Record.find(Record.class, "day = ?", day);  //Record.listAll(Record.class); //

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
                r.day = day;
                r.f_id = f.getId();
            }
            recordsToHandle.put(f,r);
        }

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
