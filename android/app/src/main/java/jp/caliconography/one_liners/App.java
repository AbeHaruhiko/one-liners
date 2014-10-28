package jp.caliconography.one_liners;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

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

        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);

        Parse.enableLocalDatastore(this);
//        ParseUser.enableAutomaticUser();

//        ParseACL.setDefaultACL(new ParseACL(), true);
    }
}
