package com.izdo.mynetease.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    ImageView add;

    // 是否显示菜单
    boolean isShowMenu = false;

    RelativeLayout menu_title;
    FrameLayout menu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        add = (ImageView) view.findViewById(R.id.add);
        menu_title = (RelativeLayout) view.findViewById(R.id.menu_title);
        menu = (FrameLayout) view.findViewById(R.id.menu);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowMenu) {
                    Animation add_up = AnimationUtils.loadAnimation(getContext(), R.anim.add_up);
                    add_up.setFillAfter(true);
                    add.startAnimation(add_up);

                    // 设置顶部布局显示
                    menu_title.setVisibility(View.VISIBLE);
                    Animation top_menu_show = AnimationUtils.loadAnimation(getContext(), R.anim.top_menu_show);
                    menu_title.startAnimation(top_menu_show);

                    menu.setVisibility(View.VISIBLE);
                    Animation from_top = AnimationUtils.loadAnimation(getContext(), R.anim.from_top);
                    menu.startAnimation(from_top);

                    isShowMenu = true;
                } else {
                    Animation add_down = AnimationUtils.loadAnimation(getContext(), R.anim.add_down);
                    add_down.setFillAfter(true);
                    add.startAnimation(add_down);

                    // 设置顶部布局显示
                    menu_title.setVisibility(View.VISIBLE);
                    Animation top_menu_hide = AnimationUtils.loadAnimation(getContext(), R.anim.top_menu_hide);
                    top_menu_hide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            menu_title.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    menu_title.startAnimation(top_menu_hide);

                    Animation to_top = AnimationUtils.loadAnimation(getContext(), R.anim.to_top);
                    to_top.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            menu.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    menu.startAnimation(to_top);

                    isShowMenu = false;
                }
            }
        });

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
