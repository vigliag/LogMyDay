package ovh.vii.logmyday;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by vigliag on 9/12/15.
 */
public class Field extends SugarRecord<Field> {

    @Ignore
    public static final int TEXT_RECORD = 1;
    @Ignore
    public static final int VALUE_RECORD = 0;

    String name;
    int weight;
    int minvalue;
    int maxvalue;
    int fieldType;

    String color;

    @Override
    public String toString() {
        return name;
    }

    public Field(){
        minvalue = 0;
        maxvalue = 10;
        fieldType = VALUE_RECORD;
    }

}
