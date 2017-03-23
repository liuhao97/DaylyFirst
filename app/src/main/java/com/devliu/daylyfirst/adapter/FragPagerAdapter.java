package com.devliu.daylyfirst.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuhao
 * on 2017/3/21
 * use to :
 */

public class FragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragList;
    private String[] titleList;

    public FragPagerAdapter(FragmentManager fm, List<Fragment> fragList, String[] titleList) {
        super(fm);
        this.fragList = fragList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }
}
