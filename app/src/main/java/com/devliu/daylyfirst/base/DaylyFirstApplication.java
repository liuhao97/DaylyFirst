package com.devliu.daylyfirst.base;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by liuhao
 * on 2017/3/10
 * use to :
 */

public class DaylyFirstApplication extends Application {

    {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        UMShareAPI.get(this);

        x.Ext.init(this);
        x.Ext.setDebug(true);
    }

    public static DbManager getDb(){
        DbManager.DaoConfig config = new DbManager.DaoConfig()
                .setDbName("dayly_first.db")
                .setDbVersion(1);

        return x.getDb(config);
    }
}
