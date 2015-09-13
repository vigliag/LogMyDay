package ovh.vii.logmyday;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by vigliag on 9/12/15.
 */
public class Record extends SugarRecord<Record> {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    long f_id;
    String date;
    int value;
    String text;

    public Record() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}