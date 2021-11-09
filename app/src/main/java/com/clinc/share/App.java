package com.clinc.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.clinc.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/JF-Flat-Regular.ttf");
//        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/JF-Flat-Regular.ttf");
//        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/JF-Flat-Regular.ttf");
//        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/JF-Flat-Regular.ttf");

    }
}

