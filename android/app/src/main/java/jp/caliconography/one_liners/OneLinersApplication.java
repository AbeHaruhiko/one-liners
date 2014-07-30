package jp.caliconography.one_liners;

import android.app.Application;

import us.costan.chrome.ChromeView;


/**
 * Created by abeharuhiko on 2014/07/30.
 */
public class OneLinersApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ChromeView.initialize(this);
    }
}
