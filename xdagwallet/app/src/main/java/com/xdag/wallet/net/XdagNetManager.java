package com.xdag.wallet.net;

import android.content.Context;
import android.content.SharedPreferences;

import com.xdag.wallet.XdagApplication;
import com.xdag.wallet.model.Constants;

/**
 * Created by wangxuguo on 2018/7/19.
 */

public class XdagNetManager {
    private static final XdagNetManager ourInstance = new XdagNetManager();

    static XdagNetManager getInstance() {
        return ourInstance;
    }

    private XdagNetManager() {
    }

    public static String getBaseUrl(){
        SharedPreferences sp = XdagApplication.getContext().getSharedPreferences(Constants.XDAG_NET, Context.MODE_PRIVATE);
        return sp.getString(Constants.XDAG_EXPLORER_BASEURL,Constants.DefaultExplorerAddress);
    }
}
