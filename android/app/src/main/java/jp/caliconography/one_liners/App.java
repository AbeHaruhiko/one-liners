package jp.caliconography.one_liners;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import jp.caliconography.one_liners.model.parseobject.Review;

/**
 * Created by abeharuhiko on 2014/10/28.
 */
public class App extends Application {

    public static final String PARSE_APPLICATION_ID = "JMUkE2OUUNB6qud14tfuDZT7o0rHxlVYCdBmNbtT";
    public static final String PARSE_CLIENT_KEY = "i0IZoD6ZRoZwumDZSfoHnJbu24Lj73J3UREPUcnV";

    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Review.class);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().saveInBackground();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
