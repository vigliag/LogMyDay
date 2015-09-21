package ovh.vii.logmyday.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orm.SugarApp;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ovh.vii.logmyday.Application;

/**
 * Created by vigliag on 9/21/15.
 */
public class Database {

    SQLiteDatabase db;

    public Database(){
        com.orm.Database sugardb = ((Application) SugarApp.getSugarContext()).obtainDatabase();
        db = sugardb.getDB();
    }

    public long fieldCount(){
        return Field.count(Field.class, null, null);
    }

    public Field getFieldById(long field_id){
        return Field.findById(Field.class, field_id);
    }

    public void saveField(Field f){
        f.save();
    }

    public void deleteFieldWithRecords(Field f){
        Record.deleteAll(Record.class, "fid = ?", String.valueOf(f.getId()));
        f.delete();
    }

    public List<Record> listRecords(){
        return Record.listAll(Record.class);
    }

    public List<Field> listFields(){
        return Field.listAll(Field.class);
    }

    public Map<Field, Record> recordsPerFieldForDay(String day){

        List<Field> fieldList = listFields();
        List<Record> recordList = Record.find(Record.class, "day = ?", day);  //Record.listAll(Record.class); //

        //index records by field_id
        Map<Long, Record> recordByField = new HashMap<>();
        for (Record r : recordList){
            recordByField.put(r.getF_id(), r);
        }

        //in memory join
        Map<Field, Record> recordsToHandle = new HashMap<>();
        for (Field f: fieldList) {
            Record r = recordByField.get(f.getId());
            if(r == null){
                r = new Record();
                r.setDay(day);
                r.setF_id(f.getId());
            }
            recordsToHandle.put(f,r);
        }

        return recordsToHandle;
    }

    public List<Field> listFieldsContainingValues(){
        return Field.find(Field.class, "field_type = ?", String.valueOf(Field.VALUE_RECORD));
    }

    public Calendar getOldestRecordDate(){
        Calendar oldestTime = GregorianCalendar.getInstance();

        try {
            //get the minimum day as string
            Cursor mindayresult = db.rawQuery("Select MIN(day) as day from RECORD", null);
            mindayresult.moveToFirst();
            String minday = mindayresult.getString(0);

            //parse it as date
            Log.d("MI", "Oldest record is: " + minday);

            if(minday != null){
                Date oldestdate = Record.dateFormat.parse(minday);
                oldestTime.setTime(oldestdate);
            }

            //daysAgo = Days.daysBetween(new DateTime(oldestdate.getTime()), new DateTime()).getDays();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return oldestTime;
    }

    public List<Record> listRecordsForField(Field f) {
        return Record.findWithQuery(Record.class,
                "Select * from RECORD where fid = ? order by day", String.valueOf(f.getId()));
    }

    public void saveRecord(Record r){
        r.save();
    }
}
