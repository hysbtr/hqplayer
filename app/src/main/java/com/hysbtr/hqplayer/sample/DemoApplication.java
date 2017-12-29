package com.hysbtr.hqplayer.sample;

import android.app.Application;

import com.hysbtr.hqplayer.sample.util.ScreenAdapter;

/**
 * Created by guoxiaodong on 2017/6/8
 */
public class DemoApplication extends Application {
    private static DemoApplication demoApplication;

    public static DemoApplication getInstance() {
        return demoApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        demoApplication = this;
        ScreenAdapter.init();
    }
}
