package ovh.vii.logmyday.data;

import android.database.Cursor;
import android.os.Environment;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by vigliag on 9/21/15.
 */
public class FileSerialization {

    public static void exportCSVToFile() throws IOException {
        Database db = new Database();

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        String curdate = Record.dateFormat.format(GregorianCalendar.getInstance().getTime());

        File file = new File(path, "ExportedRecords " + curdate +".csv");
        CSVWriter writer = new CSVWriter(new FileWriter(file));

        Cursor records = db.getCompleteRecords();
        records.moveToFirst();

        while (records.moveToNext()) {

            //r.day, f.name, r.value, r.text
            String[] row = {
                    records.getString(0),
                    records.getString(1),
                    String.valueOf(records.getInt(2)),
                    records.getString(3)
            };
            writer.writeNext(row);
        }

        records.close();
        writer.close();

    }

    public static void exportJsonToFile() throws IOException {

        Database db = new Database();

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        String curdate = Record.dateFormat.format(GregorianCalendar.getInstance().getTime());
        File file = new File(path, "ExportedRecords " + curdate +".json");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            List<Record> records = db.listRecords();
            List<Field> fields = db.listFields();

            JSONArray fieldJsonArray = new JSONArray();
            for(Field f : fields){
                fieldJsonArray.put(f.toJson());
            }

            JSONArray recordJsonArray = new JSONArray();
            for(Record record: records){
                recordJsonArray.put(record.toJson());
            }

            JSONObject result = new JSONObject();

            result.put("fields", fieldJsonArray);
            result.put("records", recordJsonArray);

            writer.write(result.toString());
            writer.close();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }
}
