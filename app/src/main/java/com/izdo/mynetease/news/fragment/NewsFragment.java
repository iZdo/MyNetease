package com.izdo.mynetease.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.adapter.NewsAdapter;
import com.izdo.mynetease.news.bean.FragmentInfo;
import com.izdo.mynetease.news.news_inner.HotFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

/**
 * Created by iZdo on 2017/9/6.
 */

public class NewsFragment extends Fragment {

    ArrayList<FragmentInfo> mFragments;
    NewsAdapter mNewsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragments = new ArrayList<>();

        FrameLayout layout = (FrameLayout) getActivity().findViewById(R.id.tabs);
        layout.addView(View.inflate(getActivity(), R.layout.include_tab, null));
        SmartTabLayout smartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.smart_tab);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        String[] titles = getResources().getStringArray(R.array.news_titles);
        for (int i = 0; i < titles.length; i++) {
            FragmentInfo info;
            if (i == 0) {
                info = new FragmentInfo(new HotFragment(), titles[i]);
            } else {
                info = new FragmentInfo(new EmptyFragment(), titles[i]);
            }
            mFragments.add(info);
        }
        mNewsAdapter = new NewsAdapter(getFragmentManager(), mFragments);
        viewPager.setAdapter(mNewsAdapter);
        smartTabLayout.setViewPager(viewPager);

    }
}
