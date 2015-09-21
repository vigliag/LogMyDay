package ovh.vii.logmyday.data;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vigliag on 9/12/15.
 */
public class Field extends SugarRecord<Field> {

    @Ignore
    public static final int TEXT_RECORD = 1;
    @Ignore
    public static final int VALUE_RECORD = 0;

    private String name;
    private int weight;
    private int minvalue;
    private int maxvalue;
    private int fieldType;

    private String color;

    @Override
    public String toString() {
        return getName();
    }

    public Field(){
        setMinvalue(0);
        setMaxvalue(10);
        setFieldType(VALUE_RECORD);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMinvalue() {
        return minvalue;
    }

    public void setMinvalue(int minvalue) {
        this.minvalue = minvalue;
    }

    public int getMaxvalue() {
        return maxvalue;
    }

    public void setMaxvalue(int maxvalue) {
        this.maxvalue = maxvalue;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", getName());
        jsonObject.put("weight", getWeight());
        jsonObject.put("id", getId());
        jsonObject.put("minvalue", getMinvalue());
        jsonObject.put("maxvalue", getMaxvalue());
        jsonObject.put("field_type", getFieldType());
        return jsonObject;
    }
}
