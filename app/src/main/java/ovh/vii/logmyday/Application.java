package ovh.vii.logmyday;

import com.orm.Database;
import com.orm.SugarApp;

/**
 * Created by vigliag on 9/13/15.
 */
public class Application extends SugarApp {
    public Database obtainDatabase(){
        return getDatabase();
    }

}
