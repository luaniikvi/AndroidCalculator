package com.example.calculator;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // Gán Context cấp độ ứng dụng vào biến tĩnh
        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
