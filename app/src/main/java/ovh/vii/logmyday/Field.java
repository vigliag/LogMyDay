package ovh.vii.logmyday;

import com.orm.SugarRecord;

/**
 * Created by vigliag on 9/12/15.
 */
public class Field extends SugarRecord<Field> {

    String name;
    int weight;
    int minvalue;
    int maxvalue;
    boolean istext;
    String color;

    @Override
    public String toString() {
        return name;
    }

    public Field(){

    }

}
