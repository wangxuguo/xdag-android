package com.xdag.wallet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


public class ToastUtil {

    public static void show(Context context, String text) {
        try {
            if (!TextUtils.isEmpty(text)) {
                showToast(context, text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Context context, int resId) {
        try {
            showToast(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showShort(Context context ,String text) {
        try {
            if (!TextUtils.isEmpty(text)) {
                showToast(context, text, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////
    //  private part
    //  内部逻辑处理
    ///////////////////////////////////////
    private static Toast mToast;

    private static void showToast(Context context, String text) {
        showToast(context, text, true);
    }

    private static void showToast(Context context, String text, boolean longLength) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private static void showToast(Context context, int resId) {
        showToast(context, resId, true);
    }

    private static void showToast(Context context, int resId, boolean longLength) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, longLength ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }
}
