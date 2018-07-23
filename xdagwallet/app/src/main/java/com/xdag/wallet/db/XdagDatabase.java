package com.xdag.wallet.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.xdag.wallet.model.XdagTransactionModel;

/**
 * Created by wangxuguo on 2018/7/8.
 */
@Database(name = XdagDatabase.NAME, version = XdagDatabase.VERSION)
public class XdagDatabase {
    //数据库名称
    public static final String NAME = "XdagDatabase";
    //数据库版本号
//        public static final int VERSION = 1;
    public static final int VERSION = 2;

//    @Migration(version = 2, database = XdagDatabase.class)
//    public static class Migration2 extends BaseMigration {
//        @Override
//        public void migrate(DatabaseWrapper database) {
//            // run some code here
//            SQLite.insert(XdagTransactionModel.class).execute();
//        }
//    }
}