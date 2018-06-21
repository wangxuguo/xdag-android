package com.xdag.wallet;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.xdag.wallet.db.DataBaseManager;

/**
 * Created by wangxuguo on 2018/6/21.
 */

public class XdagApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseManager.initDataBase(this);
    }
}
