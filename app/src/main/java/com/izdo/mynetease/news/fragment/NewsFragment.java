package com.izdo.mynetease.news.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.izdo.mynetease.R;
import com.izdo.mynetease.news.adapter.NewsAdapter;
import com.izdo.mynetease.news.adapter.ShowAdapter;
import com.izdo.mynetease.news.bean.FragmentInfo;
import com.izdo.mynetease.news.bean.ShowTabEvent;
import com.izdo.mynetease.news.news_inner.HotFragment;
import com.izdo.mynetease.util.NoScrollGridView;
import com.izdo.mynetease.util.SharedPrefrencesUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

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

    NoScrollGridView show, not_show;
    Button sort;

    ShowAdapter showAdapter;
    ShowAdapter not_showAdapter;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    // 显示标题的缓存
    final static String SHOW_CONTENT = "show";
    // 不显示标题的缓存
    final static String NOT_SHOW_CONTENT = "not_show";

    String lastTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        add = (ImageView) view.findViewById(R.id.add);
        menu_title = (RelativeLayout) view.findViewById(R.id.menu_title);
        menu = (FrameLayout) view.findViewById(R.id.menu);

        show = (NoScrollGridView) view.findViewById(R.id.show);
        show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isShowDel = showAdapter.isShowDel();
                if (isShowDel) {
                    if (i == 0) {
                        Toast.makeText(getContext(), "热门栏目不能删除", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String title = showAdapter.delATitle(i);
                    not_showAdapter.addAData(title);
                }
            }
        });

        not_show = (NoScrollGridView) view.findViewById(R.id.not_show);
        not_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean isShowDel = showAdapter.isShowDel();
                if (isShowDel) {
                    String title = not_showAdapter.delATitle(i);
                    showAdapter.addAData(title);
                }
            }
        });

        sort = (Button) view.findViewById(R.id.sort);
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdapter.setShowDel();
                boolean isShow = showAdapter.isShowDel();
                if (isShow) {
                    sort.setText("完成");
                } else {
                    sort.setText("删除排序");
                }

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowMenu) {
                    EventBus.getDefault().post(new ShowTabEvent(false));

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
                    showAdapter.setShowDelUnable();

                    EventBus.getDefault().post(new ShowTabEvent(true));

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

                    String content = showAdapter.getContent();
                    String not_content = not_showAdapter.getContent();

                    String[] newTitles = content.split("-");

                    SharedPrefrencesUtil.saveString(getContext(), SHOW_CONTENT, content);
                    SharedPrefrencesUtil.saveString(getContext(), NOT_SHOW_CONTENT, not_content);


                    if(lastTitle.equals(content)){
                        return;
                    }

                    mFragments.clear();
                    for (int i = 0; i < newTitles.length; i++) {
                        FragmentInfo info;
                        if (i == 0) {
                            info = new FragmentInfo(new HotFragment(), newTitles[i]);
                        } else {
                            info = new FragmentInfo(new EmptyFragment(), newTitles[i]);
                        }
                        mFragments.add(info);
                    }
                    mNewsAdapter.setData(mFragments);
                    smartTabLayout.setViewPager(viewPager);
                    lastTitle = content;
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
        smartTabLayout = (SmartTabLayout) getActivity().findViewById(R.id.smart_tab);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);

        String content = SharedPrefrencesUtil.getString(getActivity(), SHOW_CONTENT);

        String[] titles;
        if (TextUtils.isEmpty(content)) {
            titles = getResources().getStringArray(R.array.news_titles);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < titles.length; i++) {
                builder.append(titles[i]);
                if(i!=titles.length-1){
                    builder.append("-");
                }
            }
            //获取上次显示的标题
            lastTitle = builder.toString();
        } else {
            titles = content.split("-");
            //获取上次显示的标题
            lastTitle = content;
        }

        for (int i = 0; i < titles.length; i++) {
            FragmentInfo info;
            if (i == 0) {
                info = new FragmentInfo(new HotFragment(), titles[i]);
            } else {
                info = new FragmentInfo(new EmptyFragment(), titles[i]);
            }
            mFragments.add(info);
        }

        showAdapter = new ShowAdapter(titles, getContext());

        String not_content = SharedPrefrencesUtil.getString(getActivity(), NOT_SHOW_CONTENT);

        if (TextUtils.isEmpty(not_content)) {
            not_showAdapter = new ShowAdapter(getContext());
        } else {
            String[] not = not_content.split("-");
            not_showAdapter = new ShowAdapter(not, getContext());
        }

        show.setAdapter(showAdapter);
        not_show.setAdapter(not_showAdapter);

        mNewsAdapter = new NewsAdapter(getFragmentManager(), mFragments);
        viewPager.setAdapter(mNewsAdapter);
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setDividerColors(Color.TRANSPARENT);
    }
}
