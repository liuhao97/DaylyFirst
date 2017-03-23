package com.devliu.daylyfirst.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devliu.daylyfirst.R;
import com.devliu.daylyfirst.adapter.FragPagerAdapter;
import com.devliu.daylyfirst.base.BaseActivity;
import com.devliu.daylyfirst.frag.NewsFrag;
import com.devliu.daylyfirst.frag.VideoFrag;
import com.devliu.daylyfirst.nightmode.ChangeModeController;
import com.devliu.daylyfirst.nightmode.ChangeModeHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    public ArrayList<SnsPlatform> platforms;
    private SHARE_MEDIA[] list;
    private UMAuthListener authListener;
    private Activity mActivity;
    private Context mContext;

    @ViewInject(R.id.main_title_user_head)
    private ImageView titleUserHead;
    @ViewInject(R.id.main_tab_layout)
    private TabLayout tabLayout;
    @ViewInject(R.id.main_view_pager)
    private ViewPager viewPager;
    @ViewInject(R.id.layout_main_title)
    private LinearLayout titleLayout;
    @ViewInject(R.id.main_title_refresh)
    private ImageView titleRefresh;

    @ViewInject(R.id.main_left_drawer)
    private DrawerLayout leftDrawer;
    @ViewInject(R.id.main_drawer_account_qq)
    private ImageView drawerAccountQQ;
    @ViewInject(R.id.main_drawer_account)
    private LinearLayout drawerAccount;
    @ViewInject(R.id.main_drawer_user_info)
    private RelativeLayout drawerUserInfo;
    @ViewInject(R.id.main_drawer_userhead)
    private ImageView drawerUserHead;
    @ViewInject(R.id.main_drawer_username)
    private TextView drawerUserName;
    @ViewInject(R.id.main_drawer_usergender)
    private TextView drawerUserGender;
    @ViewInject(R.id.main_drawer_night_mode)
    private LinearLayout changeMode;
    private List<Fragment> fragList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //递归获取需要更改的控件
        ChangeModeController.getInstance().init(this, R.attr.class);
        //初始化theme
        ChangeModeController.setTheme(this, R.style.DayColorTrans, R.style.NightColorTrans);

        super.onCreate(savedInstanceState);

        initVariable();
        initViews();
        loadPager();
    }

    private void loadPager() {
        String [] pathlist =
                {"T1348647909107",
                "V9LG4B3A0",
                "T1399700447917",
                "T1348648517839",
                "T1348649079062",
                "T1348649580692",
                "T1348654060988"};

        String[] titleList = new String[]
                {"推荐", "搞笑", "足球", "娱乐", "体育", "科技", "汽车"};
        fragList = new ArrayList<>();
        for (int i = 0;i < pathlist.length;i++) {
            Bundle bundle = new Bundle();
            bundle.putString("keyurl", pathlist[i]);
            if(i==1){
                Fragment fragment = new VideoFrag();
                fragment.setArguments(bundle);
                fragList.add(fragment);
            }else{
                Fragment fragment = new NewsFrag();
                fragment.setArguments(bundle);
                fragList.add(fragment);
            }
        }


        FragPagerAdapter adapter = new FragPagerAdapter(getSupportFragmentManager(),
                fragList,
                titleList);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }

    private void initViews() {
        leftDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

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

        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCurFrag();
            }
        });
    }

    private void refreshCurFrag() {
        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = fragList.get(currentItem);
        if(fragment instanceof VideoFrag){
            VideoFrag videoFrag = (VideoFrag) fragment;
            videoFrag.autoRefresh();
        }

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

    @Event(value = {R.id.main_drawer_friend,
    R.id.main_drawer_active,
    R.id.main_drawer_collect,
    R.id.main_drawer_refeed,
    R.id.main_drawer_reinfo,
    R.id.main_drawer_discuss})
    private void onClick(View v){
        Toast.makeText(mActivity, "test", Toast.LENGTH_SHORT).show();
    }

    private void temp(){
        int currentItem = viewPager.getCurrentItem();
        viewPager.getChildAt(currentItem);
    }

}
