package jp.caliconography.one_liners;

import us.costan.chrome.ChromeView;
import android.app.Application;

public class one_liners extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ChromeView.initialize(this);
    }
}