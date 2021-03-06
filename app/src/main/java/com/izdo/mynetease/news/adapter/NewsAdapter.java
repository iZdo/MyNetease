package com.izdo.mynetease.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.izdo.mynetease.news.bean.FragmentInfo;

import java.util.ArrayList;

/**
 * Created by iZdo on 2017/9/6.
 */

public class NewsAdapter extends FragmentStatePagerAdapter {

    ArrayList<FragmentInfo> mFragments;

    public NewsAdapter(FragmentManager fm, ArrayList<FragmentInfo> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    // 刷新ViewPager必须重写getItemPosition()方法并返回POSITION_NONE
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

    public void setData(ArrayList<FragmentInfo> mFragments) {
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }
}
