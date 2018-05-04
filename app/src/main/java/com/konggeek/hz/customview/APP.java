package com.konggeek.hz.customview;

import android.app.Application;
import android.content.Context;

/**
 Created by wangtaian on 2018/5/3. */
public class APP extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
