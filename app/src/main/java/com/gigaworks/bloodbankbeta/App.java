package com.gigaworks.bloodbankbeta;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

/**
 * Created by Arch on 01-04-2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //background splash task
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));
    }
}
