package com.xdag.wallet.utils;

import android.content.Context;

import com.xdag.wallet.R;
import com.xdag.wallet.XdagApplication;
import com.xdag.wallet.XdagEvent;

/**
 * Created by wangxuguo on 2018/6/26.
 */

public class XdagUtils {

    public static String GetAuthHintString(Context context,final int eventType) {
        if(context == null){
            context = XdagApplication.getContext();
        }
        switch (eventType) {
            case XdagEvent.en_event_set_pwd:
                return context.getString(R.string.set_password);
            case XdagEvent.en_event_type_pwd:
                return context.getString(R.string.input_password);
            case XdagEvent.en_event_retype_pwd:
                return context.getString(R.string.retype_password);
            case XdagEvent.en_event_set_rdm:
                return context.getString(R.string.set_random_keys);
            default:
                return context.getString(R.string.input_password);
        }
    }
}
