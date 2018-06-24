package com.xdag.wallet;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.xdag.wallet.db.DataBaseManager;
import com.xdag.wallet.model.Constants;

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
        String sta = getSharedPreferences(Constants.SPSetting,MODE_PRIVATE).getString(Constants.LANGUAGE,"en");
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
