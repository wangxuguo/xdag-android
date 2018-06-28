package com.xdag.wallet.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by wangxuguo on 2018/6/26.
 */

public class AppUtils {
    public String getCurActivity(Context context){
        // 获取activity任务栈
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
        // 类名 .ui.mobile.activity.WebsiteLoginActivity
        String shortClassName = info.topActivity.getShortClassName();
        // 完整类名 com.haofang.testapp.ui.mobile.activity.WebsiteLoginActivity
        String className = info.topActivity.getClassName();
        String classSimpleName = info.topActivity.getShortClassName();
        // 包名  com.haofang.testapp
        String packageName = info.topActivity.getPackageName();
        return classSimpleName;
    }
}
