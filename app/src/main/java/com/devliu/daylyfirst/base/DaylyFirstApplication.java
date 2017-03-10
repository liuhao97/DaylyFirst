package com.devliu.daylyfirst.base;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by liuhao
 * on 2017/3/10
 * use to :
 */

public class DaylyFirstApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }

    public static DbManager getDb(){
        DbManager.DaoConfig config = new DbManager.DaoConfig()
                .setDbName("dayly_first.db")
                .setDbVersion(1);

        return x.getDb(config);
    }
}
