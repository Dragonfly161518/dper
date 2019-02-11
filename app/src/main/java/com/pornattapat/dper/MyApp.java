package com.pornattapat.dper;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initFont();
    }

    public void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/thsarabun.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
