package ovh.vii.logmyday.data;

import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by vigliag on 9/21/15.
 */
public class FileSerialization {

    public static void exportToFile() throws IOException {

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
