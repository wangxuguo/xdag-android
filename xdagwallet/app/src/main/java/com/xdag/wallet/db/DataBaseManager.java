package com.xdag.wallet.db;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.xdag.wallet.model.XdagTransactionModel;

public class DataBaseManager {
    public static void initDataBase(Context context){
//        FlowManager.init(new FlowConfig.Builder(context).build());
        FlowManager.init(new FlowConfig.Builder(context.getApplicationContext())
                .openDatabasesOnInit(true) .build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
//      FlowManager.init(this);//这句也可以初始化
    }


}
