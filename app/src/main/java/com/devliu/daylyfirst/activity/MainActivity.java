package com.devliu.daylyfirst.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.base.BaseActivity;
import com.devliu.daylyfirst.nightmode.ChangeModeController;
import com.devliu.daylyfirst.nightmode.ChangeModeHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    public ArrayList<SnsPlatform> platforms;
    private SHARE_MEDIA[] list;
    private UMAuthListener authListener;
    private Activity mActivity;
    private Context mContext;

    @ViewInject(R.id.main_title_user_head)
    ImageView titleUserHead;
    @ViewInject(R.id.main_left_drawer)
    DrawerLayout leftDrawer;
    @ViewInject(R.id.main_drawer_account_qq)
    ImageView drawerAccountQQ;
    @ViewInject(R.id.main_drawer_account)
    LinearLayout drawerAccount;
    @ViewInject(R.id.main_drawer_user_info)
    RelativeLayout drawerUserInfo;
    @ViewInject(R.id.main_drawer_userhead)
    ImageView drawerUserHead;
    @ViewInject(R.id.main_drawer_username)
    TextView drawerUserName;
    @ViewInject(R.id.main_drawer_usergender)
    TextView drawerUserGender;
    @ViewInject(R.id.main_drawer_night_mode)
    LinearLayout changeMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //递归获取需要更改的控件
        ChangeModeController.getInstance().init(this, R.attr.class);
        //初始化theme
        ChangeModeController.setTheme(this, R.style.DayColorTrans, R.style.NightColorTrans);

        super.onCreate(savedInstanceState);

        initVariable();
        initViews();

    }

    private void initViews() {

        titleUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        drawerAccountQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqAuthCheck();
            }
        });
        changeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });
    }

    private void changeMode() {
        if(ChangeModeHelper.MODE_DAY == ChangeModeHelper.getChangeMode(this)){
            ChangeModeController.changeNight(this, R.style.NightColorTrans);
        }else{
            ChangeModeController.changeDay(this, R.style.DayColorTrans);
        }
    }

    private void qqAuthCheck() {
        UMShareAPI.get(mContext)
                .doOauthVerify(mActivity,
                        platforms.get(0).mPlatform,
                        authListener);
    }

    private void openDrawer() {
        leftDrawer.openDrawer(Gravity.LEFT);
    }

    private void initVariable() {
        mActivity = this;
        mContext = this;

        authListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {}

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

                switch (action){
                    case ACTION_AUTHORIZE:
                        UMShareAPI.get(mContext)
                                .getPlatformInfo(mActivity,
                                        platforms.get(0).mPlatform,
                                        authListener);

                        break;
                    case ACTION_GET_PROFILE:
                        saveUserInfo();
                        showUserInfo(data);

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(mContext, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_LONG).show();
            }
        };

        platforms = new ArrayList<>();
        list = new SHARE_MEDIA[]{SHARE_MEDIA.QQ};
        initPlatforms();
    }

    private void showUserInfo(Map<String, String> data) {
        drawerAccount.setVisibility(View.GONE);
        drawerUserInfo.setVisibility(View.VISIBLE);

        drawerUserName.setText(data.get("screen_name"));
        drawerUserGender.setText(data.get("gender"));
        x.image().bind(drawerUserHead,
                data.get("profile_image_url"),
                new ImageOptions
                        .Builder()
                        .setCircular(true)
                        .setLoadingDrawableId(R.mipmap.ic_launcher_round)
                        .build());
    }

    private void saveUserInfo() {
        // TODO: 2017/3/15 储存用户信息逻辑 openId, name, gender, head
    }

    private void initPlatforms() {
        platforms.clear();
        for (SHARE_MEDIA e : list) {
            //过滤掉Generic类型
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())) {
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this)
                .onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this)
                .release();
        ChangeModeController.onDestory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this)
                .onSaveInstanceState(outState);
    }


}
