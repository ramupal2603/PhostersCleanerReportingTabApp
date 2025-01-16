package com.brinfotech.feedbacksystem;

import android.app.Application;
import android.content.ContextWrapper;

import com.birbit.android.jobqueue.JobManager;
import com.pixplicity.easyprefs.library.Prefs;



public class MyApplication extends Application {

    private static final String TAG = "MYAPPLICATION";
    private static MyApplication mAppInstance = null;
    private JobManager mainJobManager;

    public static MyApplication getInstance() {
        return mAppInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }


}
