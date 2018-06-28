package com.xdag.wallet.db;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

public class DataBaseManager {
    public static void initDataBase(Context context){
//        FlowManager.init(new FlowConfig.Builder(context).build());
        FlowManager.init(new FlowConfig.Builder(context.getApplicationContext())
                .openDatabasesOnInit(true) .build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
//      FlowManager.init(this);//这句也可以初始化
    }

    @Database(name = XdagDatabase.NAME, version = XdagDatabase.VERSION)
    public final class XdagDatabase {
        //数据库名称
        public static final String NAME = "XdagDatabase";
        //数据库版本号
        public static final int VERSION = 1;

    }
}
