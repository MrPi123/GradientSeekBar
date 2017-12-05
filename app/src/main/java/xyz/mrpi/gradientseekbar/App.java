package xyz.mrpi.gradientseekbar;

import android.app.Application;

/**
 * Created by MrPi on 2017/12/5.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new RudenessScreenHelper(this,750f).activate();

    }
}
