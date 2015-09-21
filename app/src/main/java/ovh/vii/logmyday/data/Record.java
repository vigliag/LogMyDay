package ovh.vii.logmyday.data;

import android.annotation.SuppressLint;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Record class
 * represents a Record object in the database
 *
 * Created by vigliag on 9/12/15.
 */
public class Record extends SugarRecord<Record> {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long f_id;
    private String day;
    private int value;
    private String text;

    public Record() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String date) {
        this.day = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("f_id", getF_id());
        jsonObject.put("day", getDay());
        jsonObject.put("text", getText());
        jsonObject.put("value", getValue());
        return jsonObject;
    }

    public long getF_id() {
        return f_id;
    }

    public void setF_id(long f_id) {
        this.f_id = f_id;
    }
}
