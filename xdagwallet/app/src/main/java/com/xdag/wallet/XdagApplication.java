package com.xdag.wallet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.xdag.wallet.db.DataBaseManager;
import com.xdag.wallet.model.Constants;

import io.fabric.sdk.android.Fabric;
import java.util.Locale;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class XdagApplication extends MultiDexApplication {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        DataBaseManager.initDataBase(this);

        initMutilLilingual();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        Fabric.with(this, crashlyticsKit, new Crashlytics(), new CrashlyticsNdk());
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Context getContext() {
        return mContext;
    }

    public void initMutilLilingual() {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = getSetLocale();
        resources.updateConfiguration(config, dm);
    }

    private Locale getSetLocale() {
        Locale locale = Locale.getDefault();
        String sta = getSharedPreferences(Constants.SPSetting,MODE_PRIVATE).getString(Constants.LANGUAGE,locale.getLanguage());
        Locale myLocale = new Locale(sta);
        return myLocale;
    }
//if (!LanguageUtil.isSetValue()) {
//        LanguageUtil.resetDefaultLanguage();
//    }
    /**
     * 是否是设置值
     *
     * @return 是否是设置值
     */
    public boolean isSetValue() {
        Locale currentLocale = getResources().getConfiguration().locale;
        return currentLocale.equals(getSetLocale());
    }
}
